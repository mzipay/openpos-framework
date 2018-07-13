package org.jumpmind.pos.app.state;

import org.jumpmind.pos.context.service.ContextService;
import org.jumpmind.pos.context.service.ContextServiceClient;
import org.jumpmind.pos.core.flow.IStateManager;
import org.jumpmind.pos.core.flow.ITransitionStep;
import org.jumpmind.pos.core.flow.In;
import org.jumpmind.pos.core.flow.InOut;
import org.jumpmind.pos.core.flow.ScopeType;
import org.jumpmind.pos.core.flow.Transition;
import org.jumpmind.pos.i18n.service.i18nService;
import org.jumpmind.pos.i18n.service.i18nServiceClient;
import org.jumpmind.pos.ops.service.OpsService;
import org.jumpmind.pos.ops.service.OpsServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(125)
public class InitClientsStep implements ITransitionStep {
    
    @Autowired
    ContextService contextService;
    
    @Autowired
    OpsService opsService;
    
    @Autowired
    i18nService i18nService;

    @In(scope = ScopeType.Node)
    IStateManager stateManager;
        
    @InOut(scope = ScopeType.Node)
    ContextServiceClient contextServiceClient;
    
    @InOut(scope = ScopeType.Node)
    OpsServiceClient opsServiceClient;
    
    @InOut(scope = ScopeType.Node)
    i18nServiceClient i18nServiceClient;
    
    public boolean isApplicable(Transition transition) {
        if (contextServiceClient == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void arrive(Transition transition) {
        this.contextServiceClient = new ContextServiceClient(contextService, stateManager.getNodeId());
        this.opsServiceClient = new OpsServiceClient(opsService, contextServiceClient);
        this.i18nServiceClient = new i18nServiceClient(contextServiceClient, i18nService);
        transition.proceed();
    }
}
