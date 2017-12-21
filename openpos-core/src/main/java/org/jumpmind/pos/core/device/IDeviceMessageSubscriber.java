package org.jumpmind.pos.core.device;

public interface IDeviceMessageSubscriber {
    public IDeviceResponse sendDeviceRequest(IDeviceRequest request);
}
