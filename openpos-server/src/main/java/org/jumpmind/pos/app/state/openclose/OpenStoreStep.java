package org.jumpmind.pos.app.state.openclose;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jumpmind.pos.app.state.Prerequisite;
import org.jumpmind.pos.app.state.Requires;
import org.jumpmind.pos.context.model.DeviceModel;
import org.jumpmind.pos.context.service.ContextServiceClient;
import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.flow.ActionHandler;
import org.jumpmind.pos.core.flow.IStateManager;
import org.jumpmind.pos.core.flow.ITransitionStep;
import org.jumpmind.pos.core.flow.In;
import org.jumpmind.pos.core.flow.ScopeType;
import org.jumpmind.pos.core.flow.Transition;
import org.jumpmind.pos.ops.model.UnitStatus;
import org.jumpmind.pos.ops.model.UnitStatusConstants;
import org.jumpmind.pos.ops.service.GetStatusResult;
import org.jumpmind.pos.ops.service.OpsService;
import org.jumpmind.pos.ops.service.StatusChangeRequest;
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
        Class<?> targetClass = transition.getTargetState().getClass();
        Requires requires = targetClass.getAnnotation(Requires.class);
        if (requires != null && isPrerequisite(requires, Prerequisite.OPEN_STORE)) {
            DeviceModel device = contextServiceClient.getDevice();
            GetStatusResult results = opsService.getUnitStatus(UnitStatusConstants.UNIT_TYPE_STORE, device.getBusinessUnitId());
            UnitStatus unitStatus = results.getUnitStatus(device.getBusinessUnitId());
            if (unitStatus == null || unitStatus.getUnitStatus().equals(UnitStatusConstants.STATUS_CLOSED)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void arrive(Transition transition) {
        // TODO i18n
        stateManager.getUI().askYesNo("The store is closed.  Would you like to open the store?", "OpenStore", "DoNotOpenStore");
    }

    protected boolean isPrerequisite(Requires requires, Prerequisite target) {
        Prerequisite[] values = requires.value();
        for (Prerequisite prerequisite : values) {
            if (prerequisite.equals(target)) {
                return true;
            }
        }
        return false;
    }

    @ActionHandler
    public void onOpenStore(Action action) {
        StatusChangeRequest request = new StatusChangeRequest();
        // TODO where to get the business date from?
        request.setBusinessDay(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        request.setBusinessUnitId(contextServiceClient.getDevice().getBusinessUnitId());
        request.setNewStatus(UnitStatusConstants.STATUS_OPEN);
        request.setRequestingDeviceId(contextServiceClient.getDevice().getDeviceId());
        request.setTimeOfRequest(new Date());
        request.setUnitId(request.getBusinessUnitId());
        request.setUnitType(UnitStatusConstants.UNIT_TYPE_STORE);
        opsService.updateUnitStatus(request);
        this.transition.proceed();
    }

    @ActionHandler
    public void onDoNotOpenStore(Action action) {
        this.transition.cancel();
    }

}
