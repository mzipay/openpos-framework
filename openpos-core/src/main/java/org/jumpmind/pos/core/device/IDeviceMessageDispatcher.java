package org.jumpmind.pos.core.device;


public interface IDeviceMessageDispatcher {
//    public void setDeviceMessageSubscriber(IDeviceMessageSubscriber subscriber);
    public IDeviceResponse sendDeviceRequest(IDeviceRequest request);
    //public void ping();
}
