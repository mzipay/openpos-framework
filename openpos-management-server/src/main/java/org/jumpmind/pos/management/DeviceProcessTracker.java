package org.jumpmind.pos.management;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jumpmind.pos.util.StreamCopier;
import org.jumpmind.pos.util.model.ProcessInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DeviceProcessTracker {
    
    @Autowired
    DeviceProcessStatusClient deviceProcessStatusClient;
    
    Map<String, DeviceProcessInfo> trackingMap = new HashMap<>();
    Map<String, Boolean> lockMap = new HashMap<>();

    public boolean waitForLock(String deviceId, long maxWaitMillis) {
        DeviceProcessLockWatcher watcher = null;
        synchronized(lockMap) {
            if (this.lock(deviceId)) {
                return true;
            } else {
                watcher = new DeviceProcessLockWatcher(deviceId);
                watcher.start();
            }
        }
        if (watcher != null) {
            try {
                synchronized(watcher) {
                    log.info("Waiting up to {}ms for Device Process '{}' lock", maxWaitMillis, deviceId);
                    watcher.wait(maxWaitMillis);
                    log.info("Lock status changed to unlocked or wait period expired for Device Process '{}'. lock status is: '{}'", deviceId, getDeviceLockStatus(deviceId));
                    return getDeviceLockStatus(deviceId);
                }
            } catch (InterruptedException ex) {
                log.warn("DeviceProcessLockWatcher interrupted", ex);
            } finally {
                watcher.stopRunning();
            }
        }
        
        return false;
        
        
    }
    
    @Scheduled(fixedRateString = "${openpos.managementServer.statusCheckPeriodMillis:7500}")
    public void checkDeviceProcessStatus() {
        log.trace("checkDeviceProcessStatus running");

        // Iterate over all the known devices and hit their status service to determine status
        Iterator<String> piIterator = this.trackingMap.keySet().iterator();
        while (piIterator.hasNext()) {
            String deviceId = piIterator.next();
            DeviceProcessInfo pi = this.getDeviceProcessInfo(deviceId);
            if (pi.getPort() != null) {
                DeviceProcessStatusCheckThread statusCheckThread = new DeviceProcessStatusCheckThread(deviceId, pi.getPort());
                statusCheckThread.start();
            } else {
                log.warn("No port number stored for Device Process '{}', ommitting from status tracking", pi.getDeviceId());
            }
            
        }
        
    }
    
    public void untrack(DeviceProcessInfo pi) {
        this.removeDeviceProcessInfo(pi);
    }
    
    protected DeviceProcessInfo removeDeviceProcessInfo(DeviceProcessInfo pi) {
        synchronized(trackingMap) {
            return this.trackingMap.remove(pi.getDeviceId());
        }
    }
    
    public DeviceProcessInfo updateDeviceProcessStatus(String deviceId, DeviceProcessStatus newStatus) {
        synchronized(trackingMap) {
            DeviceProcessInfo dpi = trackingMap.get(deviceId);
            dpi.setStatus(newStatus);
            return dpi;
        }        
    }

    public DeviceProcessInfo updateDeviceProcessStatus(String deviceId, DeviceProcessStatus newStatus, Integer pid) {
        synchronized(trackingMap) {
            DeviceProcessInfo dpi = trackingMap.get(deviceId);
            dpi.setStatus(newStatus);
            dpi.setPid(pid);
            return dpi;
        }        
    }
    
    public DeviceProcessInfo updateDeviceProcessPort(String deviceId, int port) {
        synchronized(trackingMap) {
            DeviceProcessInfo dpi = trackingMap.get(deviceId);
            dpi.setPort(port);
            return dpi;
        }        
    }

    public DeviceProcessInfo getDeviceProcessInfo(String deviceId) {
        return this.getDeviceProcessInfo(null, deviceId);
    }

    public DeviceProcessInfo getDeviceProcessInfo(String appId, String deviceId) {
        synchronized(trackingMap) {
            DeviceProcessInfo deviceProcessInfo = trackingMap.get(deviceId);
            if (deviceProcessInfo == null) {
                deviceProcessInfo = new DeviceProcessInfo(appId, deviceId, DeviceProcessStatus.NotRunning);
                trackingMap.put(deviceId, deviceProcessInfo);
            }

            return deviceProcessInfo;
        }        
    }
    
    public Boolean getDeviceLockStatus(String deviceId) {
        synchronized(lockMap) {
            return lockMap.get(deviceId);
        }        
    }
    
    protected boolean lock(String deviceId) {
        synchronized(lockMap) {
            Boolean lockStatus = this.getDeviceLockStatus(deviceId);
            if (lockStatus == null || ! lockStatus) {
                log.trace("Locked for {}", deviceId);
                this.lockMap.put(deviceId, Boolean.TRUE);
                return true;
            }
            
            return false;
        }        
    }
    
    public void unlock(String deviceId) {
        synchronized(lockMap) {
            Boolean lockStatus = this.getDeviceLockStatus(deviceId);
            if (lockStatus != null && lockStatus) {
                this.lockMap.put(deviceId, Boolean.FALSE);
                log.trace("Unlocked for {}", deviceId);
            }
        }        
    }
    
    public void clean() {
        synchronized (trackingMap) {
            trackingMap.clear();
        }
        synchronized (lockMap) {
            lockMap.clear();
        }
    }

    
    class DeviceProcessStatusWatcher extends Thread {
        String deviceId;
        List<DeviceProcessStatus> statuses;
        boolean stopped = false;
        
        DeviceProcessStatusWatcher(String deviceId,  DeviceProcessStatus...statuses) {
            this.deviceId = deviceId;
            this.statuses = Arrays.asList(statuses);
        }
        
        public void stopRunning() {
            this.stopped = true;
        }
        @Override
        public void run() {
            while (! stopped) {
                DeviceProcessInfo pi = DeviceProcessTracker.this.getDeviceProcessInfo(deviceId);
                if (null != pi && this.statuses.contains(pi.getStatus())) {
                    synchronized(this) {
                        this.notify();
                    }
                    return;
                }
                try {
                    sleep(1000);
                } catch (InterruptedException ex) {
                    // TODO
                }
            }
        }
    }

    class DeviceProcessLockWatcher extends Thread {
        String deviceId;
        boolean stopped = false;
        
        DeviceProcessLockWatcher(String deviceId) {
            this.deviceId = deviceId;
        }
        
        public void stopRunning() {
            this.stopped = true;
        }
        
        @Override
        public void run() {
            while (! stopped) {
                Boolean deviceLocked = DeviceProcessTracker.this.lock(deviceId);
                if (deviceLocked) {
                    synchronized(this) {
                        this.notify();
                    }
                    return;
                }
                try {
                    sleep(1000);
                } catch (InterruptedException ex) {
                    // TODO
                }
            }
        }
    }

    
    class DeviceProcessStatusCheckThread extends Thread {
        
        private String deviceId;
        private int port;
        
        public DeviceProcessStatusCheckThread(String deviceId, int port) {
            this.deviceId = deviceId;
            this.port = port;
        }
        
        @Override
        public void run() {
            DeviceProcessInfo dpi = DeviceProcessTracker.this.getDeviceProcessInfo(deviceId);
            log.trace("Running status check for Device Process '{}' on port {} (current status is: {})...", deviceId, port, dpi.getStatus());
            if (dpi.getStatus() != DeviceProcessStatus.StartupFailed) {
                boolean running = false;
                try {
                    ProcessInfo pi = DeviceProcessTracker.this.deviceProcessStatusClient.getDeviceProcessStatus(this.deviceId, this.port);
                    if (pi != null) {
                        running = true;
                        // We're transitioning from Starting to Running
                        if (dpi.getStatus() == DeviceProcessStatus.Starting) {
                            // We don't need to capture output any longer since the process
                            // is now fully started.
                            if (dpi.getStreamCopier() != null) {
                                log.debug("Stopping output to process log file for Device Process {}", deviceId);
                                dpi.getStreamCopier().stopCopying();
                            } else {
                                log.trace("StreamCopier is null for {}", deviceId);
                            }
                            log.info("Device Process '{}' status changed from {} to {}", dpi.getDeviceId(), dpi.getStatus(), DeviceProcessStatus.Running);
                        }
                        if (pi.getPid() != null) {
                            DeviceProcessTracker.this.updateDeviceProcessStatus(
                                this.deviceId,
                                DeviceProcessStatus.Running,
                                pi.getPid()
                            );
                        } else {
                            DeviceProcessTracker.this.updateDeviceProcessStatus(
                                    this.deviceId,
                                    DeviceProcessStatus.Running
                            );
                        }
                    }
                } catch (Exception ex) {
                    log.trace("Failed to get status for Device Process '{}'.  Reason: {}", deviceId, ex.getMessage());
                    running = false;
                }
                
                if (! running) {
                    if (dpi.getStatus() == DeviceProcessStatus.Starting) {
                        log.trace("Device Process '{}' is 'Starting', will check again soon...", dpi.getDeviceId());
                    } else if (dpi.getStatus() == DeviceProcessStatus.Running){
                        DeviceProcessTracker.this.updateDeviceProcessStatus(
                                this.deviceId, 
                                DeviceProcessStatus.NotRunning);
                        log.info("Device Process '{}' status changed from {} to {}", dpi.getDeviceId(), dpi.getStatus(), DeviceProcessStatus.NotRunning);
                    }
                }
            }
        }        
    }

}
