package org.jumpmind.pos.app.state.user;

import org.jumpmind.pos.context.service.ContextServiceClient;
import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.flow.ActionHandler;
import org.jumpmind.pos.core.flow.IState;
import org.jumpmind.pos.core.flow.In;
import org.jumpmind.pos.core.flow.ScopeType;

public class ManagerOverrideState extends UserLoginState {

    public ManagerOverrideState(IState sourceState, IState targetState) {
        super(sourceState, targetState);
    }

    @In(scope = ScopeType.Node)
    private ContextServiceClient contextServiceClient;

    @Override
    public void arrive(Action action) {
        notifyInsufficientPrivilege();
    }

    protected void notifyInsufficientPrivilege() {
        stateManager.getUI().askYesNo("Insufficient privilege. Provide a manager override?", "ManagerLogin", "Back");
    }

    @ActionHandler
    protected void onManagerLogin() {
        promptForLogin();
    }

}
