package org.jumpmind.pos.user.model;

import java.util.List;

import org.jumpmind.pos.persist.DBSession;
import org.jumpmind.pos.persist.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    
    private Query<PasswordHistory> passwordHistoryLookup = new Query<PasswordHistory>()
            .named("passwordHistoryLookup")
            .result(PasswordHistory.class);
    
    @Autowired
    @Qualifier("userDbSession")
    private DBSession dbSession;    
    
    public User findUser(String userName) {
        User userLookedUp = dbSession.findByNaturalId(User.class, userName);
        if (userLookedUp != null) {
            List<PasswordHistory> passwordHistory = dbSession.query(passwordHistoryLookup, userLookedUp.getUsername());
            if (passwordHistory != null) {
                userLookedUp.setPasswordHistory(passwordHistory);
            }
        }
        
        return userLookedUp;
    }

    
    public void save(User user) {
        dbSession.save(user);
        
        for (PasswordHistory passwordHistory : user.getPasswordHistory()) {
            passwordHistory.setUsername(user.getUsername());
        }
        
        dbSession.saveAll(user.getPasswordHistory());
    }
}