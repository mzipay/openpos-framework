package org.jumpmind.pos.devices;

import org.jumpmind.pos.devices.model.DeviceConfigModel;
import org.jumpmind.pos.devices.model.DeviceConfigPropModel;
import org.jumpmind.pos.devices.model.DeviceRequest;

public final class DevicesUtils {

    private DevicesUtils() {
    }

    public static String getLogicalName(DeviceConfigModel deviceModel) {
        return DevicesUtils.getLogicalName(deviceModel.getProfile(), deviceModel.getDeviceName());        
    }

    public static String getLogicalName(DeviceConfigPropModel deviceModel) {
        return DevicesUtils.getLogicalName(deviceModel.getProfile(), deviceModel.getDeviceName());        
    }

    public static String getLogicalName(DeviceRequest request) {
        return DevicesUtils.getLogicalName(request.getProfile(), request.getDeviceName());
    }

    public static String getLogicalName(String profile, String deviceName) {
        return String.format("%s_%s", profile, deviceName);
    }
}
