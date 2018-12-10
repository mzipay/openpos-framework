package org.jumpmind.pos.devices;

import org.jumpmind.pos.devices.model.DeviceModel;
import org.jumpmind.pos.devices.model.DevicePropModel;
import org.jumpmind.pos.devices.model.DeviceRequest;

public final class DevicesUtils {

    private DevicesUtils() {
    }

    public static String getLogicalName(DeviceModel deviceModel) {
        return DevicesUtils.getLogicalName(deviceModel.getProfile(), deviceModel.getDeviceName());        
    }

    public static String getLogicalName(DevicePropModel deviceModel) {
        return DevicesUtils.getLogicalName(deviceModel.getProfile(), deviceModel.getDeviceName());        
    }

    public static String getLogicalName(DeviceRequest request) {
        return DevicesUtils.getLogicalName(request.getProfile(), request.getDeviceName());
    }

    public static String getLogicalName(String profile, String deviceName) {
        return String.format("%s-%s", profile, deviceName);
    }
}
