package org.jumpmind.jumppos.core.service;

import org.jumpmind.jumppos.core.flow.Action;
import org.jumpmind.jumppos.core.screen.DefaultScreen;

public interface IScreenService {

    public void showScreen(String nodeId, DefaultScreen screen);
    
    public void refresh(String nodeId);
    
    public DefaultScreen getLastScreen(String nodeId);

    public DefaultScreen deserializeScreenPayload(String nodeId, Action action);
    
}
