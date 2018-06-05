package org.jumpmind.pos.app.state.user;

import java.util.List;

import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.flow.IState;
import org.jumpmind.pos.core.flow.IStateInterceptor;
import org.jumpmind.pos.core.flow.StateManager;
import org.jumpmind.pos.user.model.Permission;
import org.jumpmind.pos.user.model.User;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(200)
public class SecurityStateInterceptor implements IStateInterceptor {

    @Override
    public IState intercept(StateManager stateManager, IState currentState, IState newState, Action action) {
        StatePermission statePermission = newState.getClass().getDeclaredAnnotation(StatePermission.class);
        User user = stateManager.getScopeValue("currentUser");

        if (statePermission != null && user == null) {
            // Display User Login
            return new UserLoginState(currentState, newState);
        } else if (isUserPrivileged(user, statePermission)) {
            // Proceed to newState
            return null;
        } else {
            // Display Insufficient Privilege / Manager Override ?
            return new ManagerOverrideState(currentState, newState);
        }
    }

    private boolean isUserPrivileged(User user, StatePermission statePermission) {
        boolean privilege = false;
        if (statePermission == null) {
            privilege = true;
        } else if (user != null && user.getWorkgroup() != null) {
            List<Permission> permissions = user.getWorkgroup().getPermissions();

            if (permissions != null) {
                for (Permission p : permissions) {
                    if (isSufficientPermission(statePermission, p)) {
                        privilege = true;
                        break;
                    }
                }
            }
        }

        return privilege;
    }

    private boolean isSufficientPermission(StatePermission statePermission, Permission permission) {
        return permission.getPermissionId() != null && permission.getPermissionId().contains(statePermission.permissionId());
    }

}
