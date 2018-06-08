package org.jumpmind.pos.app.state.user;

import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.flow.ActionHandler;
import org.jumpmind.pos.core.flow.IState;
import org.jumpmind.pos.core.flow.Out;
import org.jumpmind.pos.core.flow.ScopeType;
import org.jumpmind.pos.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;

public class ManagerOverrideState extends UserLoginState {

    private User user;
    private StatePermission statePermission;

    @Autowired
    private StatePermissionCalculator permissionCalculator;

    public ManagerOverrideState(IState sourceState, IState targetState, User user, StatePermission statePermission) {
        super(sourceState, targetState);
        this.user = user;
        this.statePermission = statePermission;

    }

    @Out(required = false, scope = ScopeType.Conversation)
    private ManagerOverride managerOverride = new ManagerOverride();

    @Override
    public void arrive(Action action) {
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
            if (permissionCalculator.isUserPrivileged(manager, statePermission)) {
                managerOverride = new ManagerOverride();
                managerOverride.setManagerUsername(manager.getUsername());
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
        this.currentUser = user;
    }

}
