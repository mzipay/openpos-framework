package org.jumpmind.pos.app.state;

import org.jumpmind.pos.context.service.ContextService;
import org.jumpmind.pos.context.service.ContextServiceClient;
import org.jumpmind.pos.core.flow.IStateManager;
import org.jumpmind.pos.core.flow.ITransitionStep;
import org.jumpmind.pos.core.flow.In;
import org.jumpmind.pos.core.flow.InOut;
import org.jumpmind.pos.core.flow.ScopeType;
import org.jumpmind.pos.core.flow.Transition;
import org.jumpmind.pos.ops.service.OpsService;
import org.jumpmind.pos.ops.service.OpsServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(150)
public class InitClientsStep implements ITransitionStep {
    
    @Autowired
    ContextService contextService;
    
    @Autowired
    OpsService opsService;

    @In(scope = ScopeType.Node)
    IStateManager stateManager;
        
    @InOut(scope = ScopeType.Node)
    ContextServiceClient contextServiceClient;
    
    @InOut(scope = ScopeType.Node)
    OpsServiceClient opsServiceClient;
    
    public boolean isApplicable(Transition transition) {
        if (contextServiceClient == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void arrive(Transition transition) {
        contextServiceClient = new ContextServiceClient(contextService, stateManager.getNodeId());
        opsServiceClient = new OpsServiceClient(opsService, contextServiceClient);
        transition.proceed();
    }
}
