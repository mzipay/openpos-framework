package org.jumpmind.pos.core.service;

import org.jumpmind.pos.core.screen.translate.ITranslationManager;

public interface IHeadlessStartupService {

    public void startPreviouslyStarted();
    
    public void start(String nodeId);
    
    public ITranslationManager getTranslationManagerRef(String nodeId);
    
}
