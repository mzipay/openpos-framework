package org.jumpmind.pos.pos.screen.translate;

import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.screen.DefaultScreen;

public interface ITranslationManagerSubscriber {

    public void showScreen(DefaultScreen screen);

    public void doAction(Action action);

    public boolean isInTranslateState();

    public String getNodeId();
    
    public String getAppId();

}
