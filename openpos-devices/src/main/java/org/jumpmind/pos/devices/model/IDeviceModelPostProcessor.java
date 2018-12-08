package org.jumpmind.pos.devices.model;

import java.util.Map;

public interface IDeviceModelPostProcessor {

    public void postProcess(Map<String, DeviceModel> configuration);
    
}
