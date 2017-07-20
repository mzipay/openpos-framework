package org.jumpmind.jumppos.service;

import org.jumpmind.jumppos.model.Screen;

public interface IScreenService {

    public void showScreen(String clientId, Screen screen);
    
    public void refresh(String clientId);
    
}
