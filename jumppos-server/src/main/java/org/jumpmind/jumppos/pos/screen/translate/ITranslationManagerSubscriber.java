package org.jumpmind.jumppos.pos.screen.translate;

import org.jumpmind.jumppos.core.flow.Action;
import org.jumpmind.jumppos.core.screen.DefaultScreen;

public interface ITranslationManagerSubscriber {

    public void showScreen(DefaultScreen screen);

    public void doAction(Action action);

    public boolean isInTranslateState();

    public String getNodeId();

}
