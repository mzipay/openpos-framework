package org.jumpmind.jumppos.flow;

import org.jumpmind.jumppos.model.Action;

public interface IState {

    public void arrive();
    public void handleAction(Action action);
    public void setStateManager(IStateManager manager);
    
}
