package org.jumpmind.pos.devices.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jumpmind.pos.devices.model.DeviceConfigModel;
import org.jumpmind.pos.devices.model.DevicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeviceCache {

    @Autowired
    DevicesRepository deviceRepository;

    static Map<String, DeviceConfigModel> deviceModels;

    public void populate() {
        if (deviceModels == null) {
            deviceModels = new ConcurrentHashMap<>(deviceRepository.getDevices());
        }
    }

    public static Map<String, DeviceConfigModel> getDeviceModels() {
        return deviceModels;
    }

}
