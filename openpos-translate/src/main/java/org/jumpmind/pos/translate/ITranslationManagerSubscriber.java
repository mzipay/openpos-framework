package org.jumpmind.pos.translate;

import org.jumpmind.pos.core.device.IDeviceRequest;
import org.jumpmind.pos.core.device.IDeviceResponse;
import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.screen.DefaultScreen;

public interface ITranslationManagerSubscriber {

    public void showScreen(DefaultScreen screen);

    public void doAction(Action action);
    
//    public IDeviceResponse sendToDevice(IDeviceRequest request);

    public boolean isInTranslateState();

    public String getNodeId();
    
    public String getAppId();

}
