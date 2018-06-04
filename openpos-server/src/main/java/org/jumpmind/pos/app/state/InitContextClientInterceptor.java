package org.jumpmind.pos.app.state;

import org.jumpmind.pos.context.service.ContextService;
import org.jumpmind.pos.context.service.ContextServiceClient;
import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.flow.IState;
import org.jumpmind.pos.core.flow.IStateInterceptor;
import org.jumpmind.pos.core.flow.ScopeType;
import org.jumpmind.pos.core.flow.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(150)
public class InitContextClientInterceptor implements IStateInterceptor {
    
    private static final String CONTEXT_SERVICE_CLIENT = "contextServiceClient";
    
    @Autowired
    private ContextService contextService;

    @Override
    public IState intercept(StateManager stateManager, IState currentState, IState newState, Action action) {
        ContextServiceClient contextServiceClient = stateManager.getScopeValue(CONTEXT_SERVICE_CLIENT);
        if (contextServiceClient == null) {
            contextServiceClient = new ContextServiceClient(contextService, stateManager.getNodeId());
            stateManager.setScopeValue(ScopeType.Node, CONTEXT_SERVICE_CLIENT, contextServiceClient);
        }
        return null;
    }

}
