package org.jumpmind.pos.app.state.user;

import java.util.List;

import org.jumpmind.pos.user.model.Permission;
import org.jumpmind.pos.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class StatePermissionCalculator {

    public boolean isUserPrivileged(User user, StatePermission statePermission) {
        boolean privilege = false;
        if (statePermission == null) {
            privilege = true;
        } else if (user != null && user.getWorkgroup() != null) {
            List<Permission> permissions = user.getWorkgroup().getPermissions();
            if (permissions != null) {
                for (Permission p : permissions) {
                    if (isSufficientPermission(statePermission.permissionId(), p.getPermissionId())) {
                        privilege = true;
                        break;
                    }
                }
            }
        }

        return privilege;
    }

    private boolean isSufficientPermission(String requiredPermission, String userPermission) {
        userPermission = escapePermission(userPermission);
        return requiredPermission != null && userPermission != null && requiredPermission.matches(userPermission);
    }

    private String escapePermission(String userPermission) {
        if (userPermission != null) {
            userPermission = userPermission.replaceAll("\\.", "\\\\.");
            userPermission = userPermission.replaceAll("\\*", ".*");
            userPermission = userPermission.replaceAll("^", "\\^");
        }
        return userPermission;
    }

    public boolean isOverridden(StatePermission statePermission, ManagerOverride managerOverride) {
        return managerOverride != null && isSufficientPermission(statePermission.permissionId(), managerOverride.getOverridePermissionId());
    }

}
