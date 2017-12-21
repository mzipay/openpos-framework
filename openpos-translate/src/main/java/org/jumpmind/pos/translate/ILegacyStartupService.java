package org.jumpmind.pos.translate;

import org.jumpmind.pos.core.device.IDeviceMessageDispatcher;

public interface ILegacyStartupService {

    public void startPreviouslyStarted();
    
    public void start(String nodeId);
    
    public ITranslationManager getTranslationManagerRef(String nodeId);
    
    public IDeviceMessageDispatcher getDeviceMessageDispatcherRef(String nodeId);
    
}
