package org.jumpmind.pos.core.device;

public interface IDeviceManagerFactory {
    IDeviceManager create(String appId, String nodeId);
    IDeviceManager retrieve(String appId, String nodeId);
}
