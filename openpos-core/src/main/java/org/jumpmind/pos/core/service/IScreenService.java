package org.jumpmind.pos.core.service;

import org.jumpmind.pos.core.flow.ApplicationState;
import org.jumpmind.pos.core.flow.IScreenInterceptor;
import org.jumpmind.pos.core.screen.Screen;

public interface IScreenService {

    public void showScreen(String appId, String nodeId, Screen screen);
    
    public void refresh(String appId, String nodeId);
    
    public Screen getLastScreen(String appId, String nodeId);
    
    public Screen getLastDialog(String appId, String nodeId);
    
    public void setApplicationState(ApplicationState applicationState);
    
    public void addScreenInterceptor(IScreenInterceptor interceptor);
    
    public void removeScreenInterceptor(IScreenInterceptor interceptor);
    
}
