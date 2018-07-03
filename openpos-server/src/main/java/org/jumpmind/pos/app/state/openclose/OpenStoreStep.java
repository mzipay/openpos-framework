package org.jumpmind.pos.app.state.openclose;

import org.jumpmind.pos.context.model.DeviceModel;
import org.jumpmind.pos.context.service.ContextServiceClient;
import org.jumpmind.pos.core.flow.IStateManager;
import org.jumpmind.pos.core.flow.ITransitionStep;
import org.jumpmind.pos.core.flow.In;
import org.jumpmind.pos.core.flow.ScopeType;
import org.jumpmind.pos.core.flow.Transition;
import org.jumpmind.pos.ops.model.UnitStatus;
import org.jumpmind.pos.ops.model.UnitStatusConstants;
import org.jumpmind.pos.ops.service.GetStatusResult;
import org.jumpmind.pos.ops.service.OpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Order(250)
public class OpenStoreStep implements ITransitionStep {

    @Autowired
    OpsService opsService;
    
    @In(scope = ScopeType.Node)
    IStateManager stateManager;
    
    @In(scope = ScopeType.Node)
    ContextServiceClient contextServiceClient;    
    
    Transition transition;
    
    @Override
    public boolean isApplicable(Transition transition) {
        this.transition = transition;
        DeviceModel device = contextServiceClient.getDevice();
        GetStatusResult results = opsService.getUnitStatus(UnitStatusConstants.UNIT_TYPE_STORE, device.getBusinessUnitId());
        UnitStatus unitStatus = results.getUnitStatus(device.getBusinessUnitId());
        if (unitStatus == null || unitStatus.getUnitStatus().equals(UnitStatusConstants.STATUS_CLOSED)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void arrive(Transition transition) {
        // TODO i18n
        stateManager.getUI().askYesNo("The store is closed.  Would you like to open the store?", "OpenStore", "DoNotOpenStore");
    }
    
    public void onOpenStore() {
        this.transition.proceed();
    }
    
    public void onDoNotOpenStore() {
        this.transition.cancel();
    }

}
