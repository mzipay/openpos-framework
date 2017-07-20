package org.jumpmind.jumppos.flow;

import org.jumpmind.jumppos.model.Action;

public interface IStateManager {   

    public void refreshScreen();
    
    public void onAction(Action action);
    
}
