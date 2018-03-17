package org.jumpmind.pos.translate;

import java.util.Properties;

import org.jumpmind.pos.core.device.IDeviceMessageDispatcher;
import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.screen.AbstractScreen;

public interface ITranslationManagerSubscriber extends IDeviceMessageDispatcher {

    public void showScreen(AbstractScreen screen);

    public void doAction(Action action);
    
    public boolean isInTranslateState();

    public String getNodeId();
    
    public String getAppId();
    
    public Properties getProperties();

}
