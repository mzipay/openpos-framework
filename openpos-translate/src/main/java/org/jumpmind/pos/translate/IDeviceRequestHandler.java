package org.jumpmind.pos.translate;

import org.jumpmind.pos.core.device.IDeviceRequest;
import org.jumpmind.pos.core.device.IDeviceResponse;

public interface IDeviceRequestHandler {

    public IDeviceResponse sendDeviceRequest(ITranslationManagerSubscriber subscriber, TranslationManagerServer tmServer, IDeviceRequest request);
}
