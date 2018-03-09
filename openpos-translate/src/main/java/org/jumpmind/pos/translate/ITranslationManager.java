package org.jumpmind.pos.translate;

import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.model.Form;

public interface ITranslationManager {

    public void setTranslationManagerSubscriber(ITranslationManagerSubscriber subscriber);
    
    public void doAction(String appId, Action action, Form formResults);
    
    public void showActiveScreen();
    
    public void ping();
    
}
