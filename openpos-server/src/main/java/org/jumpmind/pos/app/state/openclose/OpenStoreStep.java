package org.jumpmind.pos.app.state.openclose;

import org.jumpmind.pos.app.state.Prerequisite;
import org.jumpmind.pos.app.state.Requires;
import org.jumpmind.pos.context.service.ContextServiceClient;
import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.flow.ActionHandler;
import org.jumpmind.pos.core.flow.IStateManager;
import org.jumpmind.pos.core.flow.ITransitionStep;
import org.jumpmind.pos.core.flow.In;
import org.jumpmind.pos.core.flow.ScopeType;
import org.jumpmind.pos.core.flow.Transition;
import org.jumpmind.pos.ops.service.OpsService;
import org.jumpmind.pos.ops.service.OpsServiceClient;
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

    @In(scope = ScopeType.Node)
    OpsServiceClient opsServiceClient;

    Transition transition;

    @Override
    public boolean isApplicable(Transition transition) {
        this.transition = transition;
        Class<?> targetClass = transition.getTargetState().getClass();
        Requires requires = targetClass.getAnnotation(Requires.class);
        return requires != null && isPrerequisite(requires, Prerequisite.OPEN_STORE) && !opsServiceClient.isStoreOpen();
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
        this.opsServiceClient.openStore();
        this.transition.proceed();
    }

    @ActionHandler
    public void onDoNotOpenStore(Action action) {
        this.transition.cancel();
    }

}
