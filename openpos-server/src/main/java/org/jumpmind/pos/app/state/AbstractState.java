package org.jumpmind.pos.app.state;

import org.jumpmind.pos.context.service.ContextService;
import org.jumpmind.pos.core.flow.IState;
import org.jumpmind.pos.core.flow.IStateManager;
import org.jumpmind.pos.core.flow.In;
import org.jumpmind.pos.core.flow.ScopeType;
import org.jumpmind.pos.i18n.service.i18nService;
import org.springframework.beans.factory.annotation.Autowired;

abstract public class AbstractState implements IState {

    @In(scope=ScopeType.Node)
    protected IStateManager stateManager;
    
    @Autowired
    protected i18nService i18nService;
    
    @Autowired
    protected ContextService contextService;
    
    protected String getStoreId() {        
        String nodeId = stateManager.getNodeId();
        String[] parts = nodeId.split("-");
        return parts[0];
    }
    
    protected String getWorstationId() {        
        String nodeId = stateManager.getNodeId();
        String[] parts = nodeId.split("-");
        return parts[1];
    }
    
    protected abstract String getDefaultBundleName();
    
    protected String resource(String key) {
        // i18nService.getString("user", "_loginUserId", locale, brand)
        return key;
    }
    
    protected String commonResource(String key) {
        // i18nService.getString("common", "_loginUserId", locale, brand)
        return key;
    }

    
}
