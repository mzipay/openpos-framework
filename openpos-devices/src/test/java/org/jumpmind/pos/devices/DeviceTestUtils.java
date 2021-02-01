package org.jumpmind.pos.devices;

import org.jumpmind.pos.devices.model.DeviceModel;
import org.jumpmind.pos.util.clientcontext.ClientContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeviceTestUtils {

    @Autowired
    private ClientContext clientContext;

    public void setTestDevice( String deviceId, String appId) {
        setTestDevice(deviceId, appId, DeviceModel.DEVICE_MODE_DEFAULT);
    }

    public void setTestDevice( String deviceId, String appId, String deviceMode) {
        clientContext.put("deviceId", deviceId);
        clientContext.put( "appId", appId);
        clientContext.put( "deviceMode", deviceMode);
    }

    public void setTimezoneOffset(String offset){
        clientContext.put("timezoneOffset", offset);
    }

    public void reset(){
        clientContext.clear();
    }
}
