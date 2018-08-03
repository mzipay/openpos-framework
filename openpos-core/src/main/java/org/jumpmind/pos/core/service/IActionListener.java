package org.jumpmind.pos.core.service;

import java.util.Collection;

import org.jumpmind.pos.core.flow.Action;

public interface IActionListener {

    Collection<String> getRegisteredTypes();
    
    void actionOccured(String appId, String deviceId, Action action);
    
}
