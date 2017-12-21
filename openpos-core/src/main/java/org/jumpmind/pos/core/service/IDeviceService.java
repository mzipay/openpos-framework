package org.jumpmind.pos.core.service;

import java.util.function.Consumer;

import org.jumpmind.pos.core.device.IDeviceRequest;
import org.jumpmind.pos.core.device.IDeviceResponse;

public interface IDeviceService {
    void send(String appId, String nodeId, IDeviceRequest request, Consumer<IDeviceResponse> responseHandler);
}
