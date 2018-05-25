package org.jumpmind.pos.user.model;

import java.util.List;

import org.jumpmind.pos.persist.DBSession;
import org.jumpmind.pos.persist.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository
@DependsOn(value = { "UserModule" })
public class UserRepository {
    
    private Query<PasswordHistory> passwordHistoryLookup = new Query<PasswordHistory>()
            .named("passwordHistoryLookup")
            .result(PasswordHistory.class);
    
    @Autowired
    @Lazy
    private DBSession userSession;    
    
    public User findUser(String userName) {
        User userLookedUp = userSession.findByNaturalId(User.class, userName);
        if (userLookedUp != null) {
            List<PasswordHistory> passwordHistory = userSession.query(passwordHistoryLookup, userLookedUp.getUsername());
            if (passwordHistory != null) {
                userLookedUp.setPasswordHistory(passwordHistory);
            }
        }
        
        // Add Workgroup to User
        // select Workgroup and populate in User
        // select Permisssions for Workgroup by adding query to yaml file.
        //  select columns from permision p join workgroup_permission wp on p.permission_id=wp.permission_id where wp.workgroup_id=
        // add permissions to the workgroup we just selected and added to User
        
        return userLookedUp;
    }

    
    public void save(User user) {
        userSession.save(user);
        
        for (PasswordHistory passwordHistory : user.getPasswordHistory()) {
            passwordHistory.setUsername(user.getUsername());
        }
        
        userSession.saveAll(user.getPasswordHistory());
    }
}