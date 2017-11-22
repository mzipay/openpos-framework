package org.jumpmind.pos.core.flow;

public interface IStateManagerFactory {

    IStateManager create(String appId, String nodeId);
    
    IStateManager retreive(String appId, String nodeId);
    
}
