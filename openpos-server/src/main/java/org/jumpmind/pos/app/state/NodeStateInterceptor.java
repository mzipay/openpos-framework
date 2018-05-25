package org.jumpmind.pos.app.state;

import org.jumpmind.pos.context.model.Node;
import org.jumpmind.pos.context.service.ContextService;
import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.flow.IState;
import org.jumpmind.pos.core.flow.IStateInterceptor;
import org.jumpmind.pos.core.flow.ScopeType;
import org.jumpmind.pos.core.flow.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(100)
public class NodeStateInterceptor implements IStateInterceptor {

    @Autowired
    protected ContextService contextService;
    
    @Override
    public IState intercept(StateManager stateManager, IState currentState, IState newState, Action action) {
        if (stateManager.getScopeValue("node") == null) {
            String nodeId = stateManager.getNodeId();
            Node node = contextService.getNode(nodeId).getNode();
            if (node != null) {
                stateManager.setScopeValue(ScopeType.Node, "node", node);
            } else {
                throw new IllegalStateException(String.format("Could not find a row in ctx_node with an id of %s.  It is required", nodeId));
            }
        }
        return newState;
    }

}
