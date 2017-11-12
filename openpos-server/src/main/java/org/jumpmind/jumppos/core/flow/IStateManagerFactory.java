package org.jumpmind.jumppos.core.flow;

public interface IStateManagerFactory {

    IStateManager create(String appId, String nodeId);
    
    IStateManager retreive(String appId, String nodeId);
    
}
