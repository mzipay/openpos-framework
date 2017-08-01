package org.jumpmind.jumppos.core.flow;

import org.jumpmind.jumppos.core.model.IScreen;

public interface IScreenService {

    public void showScreen(String nodeId, IScreen screen);
    
    public void refresh(String nodeId);
    
    public IScreen getLastScreen(String nodeId);
    
}
