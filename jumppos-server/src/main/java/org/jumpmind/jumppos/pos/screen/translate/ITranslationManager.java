package org.jumpmind.jumppos.pos.screen.translate;

import org.jumpmind.jumppos.core.flow.Action;
import org.jumpmind.jumppos.core.screen.DefaultScreen;

public interface ITranslationManager {

    public void setTranslationManagerSubscriber(ITranslationManagerSubscriber subscriber);
    
    public void doAction(String appId, Action action, DefaultScreen screen);
    
    public void showActiveScreen();
    
    public void ping();
    
}
