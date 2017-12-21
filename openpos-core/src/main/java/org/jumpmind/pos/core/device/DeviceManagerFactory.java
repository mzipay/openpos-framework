package org.jumpmind.pos.core.device;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class DeviceManagerFactory implements IDeviceManagerFactory {

    private Map<String, Map<String, DeviceManager>> deviceManagersByAppIdByNodeId = new HashMap<>();
    
    @Autowired
    ApplicationContext applicationContext;
    
    @Override
    public IDeviceManager create(String appId, String nodeId) {
        Map<String, DeviceManager> deviceManagersByNodeId = deviceManagersByAppIdByNodeId.get(appId);
        if (deviceManagersByNodeId == null) {
            synchronized (this) {
                if (deviceManagersByNodeId == null) {
                    deviceManagersByNodeId = new HashMap<>();
                    deviceManagersByAppIdByNodeId.put(appId, deviceManagersByNodeId);
                }
            }
        }

        DeviceManager deviceManager = deviceManagersByNodeId.get(nodeId);
        if (deviceManager == null) {
            synchronized (this) {
                if (deviceManager == null) {
                    deviceManager = applicationContext.getBean(DeviceManager.class);
                    // TODO: Not sure init is needed yet
                    //deviceManager.init(appId, nodeId);
                    deviceManagersByNodeId.put(nodeId, deviceManager);
                }
            }
        }
        return deviceManager;
    }

    @Override
    public IDeviceManager retrieve(String appId, String nodeId) {
        Map<String, DeviceManager> deviceManagersByNodeId = deviceManagersByAppIdByNodeId.get(appId);
        if (deviceManagersByNodeId != null) {
            return deviceManagersByNodeId.get(nodeId);
        } else {
            return null;
        }
    }

}
