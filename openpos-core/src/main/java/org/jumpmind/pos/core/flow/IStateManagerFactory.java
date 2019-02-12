package org.jumpmind.pos.core.flow;

import java.util.Map;

public interface IStateManagerFactory {

    IStateManager create(String appId, String nodeId, Map<String, Object> queryParams);
    
    IStateManager retrieve(String appId, String nodeId);
    
    void removeSessionIdVariables(String sessionId);
    
}
