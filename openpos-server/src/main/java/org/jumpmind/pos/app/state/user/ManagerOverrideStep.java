package org.jumpmind.pos.app.state.user;

import org.jumpmind.pos.core.flow.ActionHandler;
import org.jumpmind.pos.core.flow.In;
import org.jumpmind.pos.core.flow.Out;
import org.jumpmind.pos.core.flow.ScopeType;
import org.jumpmind.pos.core.flow.Transition;
import org.jumpmind.pos.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(200)
public class ManagerOverrideStep extends UserLoginStep {

    @Autowired
    private StatePermissionCalculator permissionCalculator;
    
    @In(scope=ScopeType.Session, required=false)
    private User currentUser;
    
    @Out(scope=ScopeType.Conversation, required=false)
    private ManagerOverride managerOverride;

    private StatePermission statePermission;
    
    public boolean isApplicable(Transition transition) {
        this.transition = transition;
        this.statePermission = transition.getTargetState().getClass().getDeclaredAnnotation(StatePermission.class);
        if (statePermission != null 
                && currentUser != null
                && ! permissionCalculator.isUserPrivileged(currentUser, statePermission)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void arrive(Transition transition) {
        promptManagerOverride();            
    }

    protected void promptManagerOverride() {
        stateManager.getUI().askYesNo("Insufficient privilege. Provide a manager override?", "ManagerLogin", "Back");
    }

    protected void notifyInsufficientPrivilege() {
        stateManager.getUI().notify("Insufficient privilege. Please provide a different user.", "ManagerLogin");
    }

    @ActionHandler
    protected void onManagerLogin() {
        promptForLogin();
    }

    @Override
    protected void processResult() {
        if (isResultSuccessful()) {
            User manager = getResultUser();
            managerOverride = new ManagerOverride();
            managerOverride.setManagerUsername(manager.getUsername());
            if (permissionCalculator.isUserPrivileged(manager, statePermission)) {
                managerOverride.setOverridePermissionId(statePermission.permissionId());
                super.processResult();
            } else {
                notifyInsufficientPrivilege();
            }
        } else {
            super.processResult();
        }
    }

    @Override
    protected void onSuccessfulAuthentication() {
        transition.proceed();
    }

}
