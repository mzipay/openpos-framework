package org.jumpmind.pos.core.device;

import org.springframework.stereotype.Component;

@Component()
@org.springframework.context.annotation.Scope("prototype")
public class DeviceManager implements IDeviceManager {

    @Override
    public IDeviceResponse send(IDeviceRequest request) {
        // TODO Auto-generated method stub
        return null;
    }

}
