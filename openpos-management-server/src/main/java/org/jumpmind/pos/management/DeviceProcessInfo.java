package org.jumpmind.pos.management;

import java.time.Instant;

import lombok.Getter;

public class DeviceProcessInfo {
    @Getter
    private String deviceId;
    @Getter
    private Integer port;
    @Getter
    private Integer pid;
    @Getter
    private DeviceProcessStatus status;
    @Getter
    private Process process;
    @Getter
    private Instant lastUpdated;

    public DeviceProcessInfo(String deviceId, DeviceProcessStatus status) {
        this.deviceId = deviceId;
        this.status = status;
        touch();
    }
    
    public DeviceProcessInfo(String deviceId, Integer port, Integer pid, DeviceProcessStatus status) {
        this(deviceId, status);
        this.port = port;
        this.pid = pid;
    }

    public DeviceProcessInfo(String deviceId, Integer port, Integer pid, DeviceProcessStatus status, Process process) {
        this(deviceId, port, pid, status);
        this.process = process;
    }
    
    public boolean isPreviouslyStarted() {
        return this.getProcess() != null;
    }

    public boolean isNotPreviouslyStarted() {
        return ! isPreviouslyStarted();
    }
    
    public boolean isProcessAlive() {
        return this.getProcess() != null &&
               this.getProcess().isAlive();
    }
    
    public void setProcess(Process process) {
        this.process = process;
        touch();
    }

    public void setPid(Integer pid) {
        this.pid = pid;
        touch();
    }

    public void setPort(Integer port) {
        this.port = port;
        touch();
    }
    
    private void touch() {
        this.lastUpdated = Instant.now();
    }
}
