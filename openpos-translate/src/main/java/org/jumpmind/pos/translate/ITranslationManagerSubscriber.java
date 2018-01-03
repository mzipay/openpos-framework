package org.jumpmind.pos.translate;

import org.jumpmind.pos.core.device.IDeviceMessageDispatcher;
import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.screen.DefaultScreen;

public interface ITranslationManagerSubscriber extends IDeviceMessageDispatcher {

    public void showScreen(DefaultScreen screen);

    public void doAction(Action action);
    
    public boolean isInTranslateState();

    public String getNodeId();
    
    public String getAppId();

}
