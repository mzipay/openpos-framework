package org.jumpmind.pos.translate;

public interface IHeadlessStartupService {

    public void startPreviouslyStarted();
    
    public void start(String nodeId);
    
    public ITranslationManager getTranslationManagerRef(String nodeId);
    
}
