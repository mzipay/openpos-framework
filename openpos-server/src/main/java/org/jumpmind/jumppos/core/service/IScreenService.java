package org.jumpmind.jumppos.core.service;

import org.jumpmind.jumppos.core.flow.Action;
import org.jumpmind.jumppos.core.screen.DefaultScreen;

public interface IScreenService {

    public void showScreen(String appId, String nodeId, DefaultScreen screen);
    
    public void refresh(String appId, String nodeId);
    
    public DefaultScreen getLastScreen(String appId, String nodeId);
    
    public DefaultScreen deserializeScreenPayload(String appId, String nodeId, Action action);

}
