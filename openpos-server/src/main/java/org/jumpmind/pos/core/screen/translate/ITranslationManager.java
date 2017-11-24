package org.jumpmind.pos.core.screen.translate;

import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.screen.DefaultScreen;

public interface ITranslationManager {

    public void setTranslationManagerSubscriber(ITranslationManagerSubscriber subscriber);
    
    public void doAction(String appId, Action action, DefaultScreen screen);
    
    public void showActiveScreen();
    
    public String getActiveScreenID();
    
    public void ping();
    
}
