package org.jumpmind.pos.devices.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jumpmind.pos.devices.DevicesUtils;
import org.jumpmind.pos.persist.DBSession;
import org.jumpmind.pos.persist.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository
@DependsOn(value = { "DevicesModule" })
public class DevicesRepository {

    @Autowired
    @Qualifier("devSession")
    @Lazy
    DBSession dbSession;

    Query<DevicePropModel> devicePropsQuery = new Query<DevicePropModel>().named("devicePropsLookupByProfile").result(DevicePropModel.class);

    public Map<String, DeviceModel> getDevices() {
        Map<String, DeviceModel> byDeviceId = new HashMap<>();
        List<DeviceModel> devices = dbSession.findAll(DeviceModel.class);
        List<DevicePropModel> properties = dbSession.findAll(DevicePropModel.class);
        for (DeviceModel deviceModel : devices) {
            byDeviceId.put(DevicesUtils.getLogicalName(deviceModel), deviceModel);
        }

        for (DevicePropModel devicePropsModel : properties) {
            DeviceModel deviceModel = byDeviceId.get(DevicesUtils.getLogicalName(devicePropsModel));
            if (deviceModel != null) {
                deviceModel.add(devicePropsModel);
            }
        }

        return byDeviceId;
    }

}
