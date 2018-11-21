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

    Query<DevicePropsModel> devicePropsQuery = new Query<DevicePropsModel>().named("deviceLookupByProfile").result(DevicePropsModel.class);

    public Map<String, DeviceModel> getDevices(String profile) {
        Map<String, DeviceModel> byDeviceId = new HashMap<>();
        List<DeviceModel> devices = dbSession.query(deviceQuery, profile);
        List<DevicePropsModel> properties = dbSession.query(devicePropsQuery, profile);
        for (DeviceModel deviceModel : devices) {
            byDeviceId.put(deviceModel.getDeviceName(), deviceModel);
        }

        for (DevicePropsModel devicePropsModel : properties) {
            DeviceModel deviceModel = byDeviceId.get(devicePropsModel.getDeviceName());
            if (deviceModel != null) {
                deviceModel.add(devicePropsModel);
            }
        }

        return byDeviceId;
    }

}
