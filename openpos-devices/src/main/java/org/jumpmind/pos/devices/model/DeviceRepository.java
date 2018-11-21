package org.jumpmind.pos.devices.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jumpmind.pos.persist.DBSession;
import org.jumpmind.pos.persist.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository
@DependsOn(value = { "DeviceModule" })
public class DeviceRepository {

    @Autowired
    @Qualifier("devSession")
    @Lazy
    DBSession dbSession;

    Query<DeviceModel> deviceQuery = new Query<DeviceModel>().named("deviceLookupByProfile").result(DeviceModel.class);

    Query<DevicePropModel> devicePropsQuery = new Query<DevicePropModel>().named("devicePropsLookupByProfile").result(DevicePropModel.class);

    public Map<String, DeviceModel> getDevices(String profile) {
        Map<String, DeviceModel> byDeviceId = new HashMap<>();
        List<DeviceModel> devices = dbSession.query(deviceQuery, profile);
        List<DevicePropModel> properties = dbSession.query(devicePropsQuery, profile);
        for (DeviceModel deviceModel : devices) {
            byDeviceId.put(deviceModel.getDeviceName(), deviceModel);
        }

        for (DevicePropModel devicePropsModel : properties) {
            DeviceModel deviceModel = byDeviceId.get(devicePropsModel.getDeviceName());
            if (deviceModel != null) {
                deviceModel.add(devicePropsModel);
            }
        }

        return byDeviceId;
    }

}
