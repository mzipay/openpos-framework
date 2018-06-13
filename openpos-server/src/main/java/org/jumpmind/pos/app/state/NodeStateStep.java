package org.jumpmind.pos.app.state;

import org.jumpmind.pos.context.model.DeviceModel;
import org.jumpmind.pos.context.service.ContextService;
import org.jumpmind.pos.context.service.DeviceResult;
import org.jumpmind.pos.core.flow.IStateManager;
import org.jumpmind.pos.core.flow.ITransitionStep;
import org.jumpmind.pos.core.flow.In;
import org.jumpmind.pos.core.flow.InOut;
import org.jumpmind.pos.core.flow.ScopeType;
import org.jumpmind.pos.core.flow.Transition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(100)
public class NodeStateStep implements ITransitionStep {

    @Autowired
    protected ContextService contextService;

    @In(scope = ScopeType.Node)
    protected IStateManager stateManager;

    @InOut(scope = ScopeType.Node, required=false)
    protected DeviceModel node;    

    @Override
    public boolean isApplicable(Transition transition) {
        return (node == null);
    }
    @Override
    public void arrive(Transition transition) {        
        String nodeId = stateManager.getNodeId();
        DeviceResult deviceResult = contextService.getDevice(nodeId);
        if (deviceResult != null) {
            this.node = deviceResult.getDevice();
        } else {
            throw new IllegalStateException(String.format("Could not find a row in ctx_device with an id of %s.  It is required.", nodeId));
        }

        transition.proceed();
    }
}
