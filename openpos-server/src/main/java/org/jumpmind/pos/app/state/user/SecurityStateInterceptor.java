package org.jumpmind.pos.app.state.user;

import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.flow.IState;
import org.jumpmind.pos.core.flow.IStateInterceptor;
import org.jumpmind.pos.core.flow.StateManager;
import org.jumpmind.pos.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(200)
public class SecurityStateInterceptor implements IStateInterceptor {

    @Autowired
    private StatePermissionCalculator permissionCalculator;

    @Override
    public IState intercept(StateManager stateManager, IState currentState, IState newState, Action action) {
        StatePermission statePermission = newState.getClass().getDeclaredAnnotation(StatePermission.class);
        User user = stateManager.getScopeValue("currentUser");
        ManagerOverride managerOverride = stateManager.getScopeValue("managerOverride");

        if (statePermission != null && user == null) {
            // Display User Login
            return new UserLoginState(currentState, newState);
        } else if (permissionCalculator.isUserPrivileged(user, statePermission)
                || permissionCalculator.isOverridden(statePermission, managerOverride)) {
            // Proceed to newState
            return null;
        } else {
            // Display Insufficient Privilege / Manager Override ?
            return new ManagerOverrideState(currentState, newState, user, statePermission);
        }
    }

}
