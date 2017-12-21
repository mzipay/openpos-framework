package org.jumpmind.pos.translate;

import org.jumpmind.pos.core.device.IDeviceMessageSubscriber;
import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.screen.DefaultScreen;

public interface ITranslationManager {

    public void setTranslationManagerSubscriber(ITranslationManagerSubscriber subscriber);
    
//    public void setDeviceMessageSubscriber(IDeviceMessageSubscriber subscriber);
    
    public void doAction(String appId, Action action, DefaultScreen screen);
    
    public void showActiveScreen();
    
    public void ping();
    
}
