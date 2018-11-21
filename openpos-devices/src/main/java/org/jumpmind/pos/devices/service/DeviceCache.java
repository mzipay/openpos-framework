package org.jumpmind.pos.devices.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jumpmind.pos.devices.model.DeviceModel;
import org.jumpmind.pos.devices.model.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeviceCache {

    @Autowired
    DeviceRepository deviceRepository;

    static Map<String, DeviceModel> deviceModels;

    public void populate(String profile) {
        if (deviceModels == null) {
            deviceModels = new ConcurrentHashMap<>(deviceRepository.getDevices(profile));
        }
    }

    public static Map<String, DeviceModel> getDeviceModels() {
        return deviceModels;
    }

}
