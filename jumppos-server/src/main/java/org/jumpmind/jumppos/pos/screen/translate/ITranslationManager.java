package org.jumpmind.jumppos.pos.screen.translate;

import org.jumpmind.jumppos.core.flow.Action;

public interface ITranslationManager {

    public void setTranslationManagerSubscriber(ITranslationManagerSubscriber subscriber);
    
    public void doAction(Action action);
    
    public void showActiveScreen();
    
}
