package org.jumpmind.pos.core.service;

import org.jumpmind.pos.core.screen.AbstractScreen;

public interface IScreenService {

    public void showScreen(String appId, String nodeId, AbstractScreen screen);
    
    public void refresh(String appId, String nodeId);
    
    public AbstractScreen getLastScreen(String appId, String nodeId);
    
    public AbstractScreen getLastDialog(String appId, String nodeId);
    
}
