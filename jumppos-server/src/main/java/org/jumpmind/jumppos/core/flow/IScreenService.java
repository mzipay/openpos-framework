package org.jumpmind.jumppos.core.flow;

import org.jumpmind.jumppos.core.model.Screen;

public interface IScreenService {

    public void showScreen(String nodeId, Screen screen);
    
    public void refresh(String nodeId);
    
    public Screen getLastScreen(String nodeId);
    
}
