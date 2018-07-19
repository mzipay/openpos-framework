package org.jumpmind.pos.user.model;

import java.util.List;

import org.jumpmind.pos.persist.DBSession;
import org.jumpmind.pos.persist.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.jumpmind.pos.user.model.UserModel;
import org.jumpmind.pos.user.model.PermissionModel;
import org.jumpmind.pos.user.model.WorkgroupModel;

@Repository
@DependsOn(value = { "UserModule" })
public class UserRepository {

    private Query<PasswordHistoryModel> passwordHistoryLookup = new Query<PasswordHistoryModel>().named("passwordHistoryLookup")
            .result(PasswordHistoryModel.class);
    
    private Query<PermissionModel> workgroupPermissionsLookup = new Query<PermissionModel>().named("workgroupPermissionsLookup")
    		.result(PermissionModel.class);
    
    @Autowired
    @Lazy
    private DBSession userSession;

    public UserModel findUser(String userName) {
        UserModel userLookedUp = userSession.findByNaturalId(UserModel.class, userName);
        if (userLookedUp != null) {
            List<PasswordHistoryModel> passwordHistory = userSession.query(passwordHistoryLookup, userLookedUp.getUsername());
            if (passwordHistory != null) {
                userLookedUp.setPasswordHistory(passwordHistory);
            }
        }

        if (userLookedUp != null) {
            String workgroupId = userLookedUp.getWorkgroupId();
            if (workgroupId != null) {
                WorkgroupModel workgroup = userSession.findByNaturalId(WorkgroupModel.class, workgroupId);
                List<PermissionModel> permissions = userSession.query(workgroupPermissionsLookup, workgroupId);
                if (workgroup != null) {
                    if (permissions != null) {
                        workgroup.setPermissions(permissions);
                    }
                    userLookedUp.setWorkgroup(workgroup);
                }
            }
        }
        return userLookedUp;
    }

    public void save(UserModel user) {
        userSession.save(user);

        for (PasswordHistoryModel passwordHistory : user.getPasswordHistory()) {
            passwordHistory.setUsername(user.getUsername());
        }

        userSession.saveAll(user.getPasswordHistory());
    }
}