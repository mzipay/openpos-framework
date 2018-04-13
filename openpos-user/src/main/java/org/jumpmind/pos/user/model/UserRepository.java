package org.jumpmind.pos.user.model;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.jumpmind.pos.persist.DBSession;
import org.jumpmind.pos.persist.DBSessionFactory;
import org.jumpmind.pos.persist.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    
    private Query<PasswordHistory> passwordHistoryLookup = new Query<PasswordHistory>()
            .named("passwordHistoryLookup")
            .result(PasswordHistory.class);
    
    @Autowired
    @Qualifier("userSessionFactory")
    private DBSessionFactory dbFactory;
    
//    public void setDbSession(DBSession db) {
//        this.db = db;
//    }
    
    public User findUser(String userName) {
        DBSession db = dbFactory.createDbSession();
        User userLookedUp = db.findByNaturalId(User.class, userName);
        if (userLookedUp != null) {
            List<PasswordHistory> passwordHistory = db.query(passwordHistoryLookup, userLookedUp.getUsername());
            if (passwordHistory != null) {
                userLookedUp.setPasswordHistory(passwordHistory);
            }
        }
        
        return userLookedUp;
//        if (userName.startsWith("u")) {
//           User user = new User();
//           user.setUsername(userName);
//           user.setFirstName("Uwe");
//           user.setLastName("Unger");
//           return user;
//        } else if (userName.startsWith("p")) {
//            User user = new User();
//            user.setUsername(userName);
//            user.setFirstName("Peter");
//            user.setLastName("Parker");
//            PasswordHistory passwordHistory = new PasswordHistory();
//            passwordHistory.setCreateTime(DateUtils.addDays(new Date(), -60));
//            passwordHistory.setExpirationTime(DateUtils.addDays(new Date(), 3));
//            user.getPasswordHistory().add(passwordHistory);
//            return user;            
//        } else if (userName.startsWith("e")) {
//            User user = new User();
//            user.setUsername(userName);
//            user.setFirstName("Earlang");
//            user.setLastName("Estefon");
//            user.setPasswordExpiredFlag(true);
//            PasswordHistory passwordHistory = new PasswordHistory();
//            passwordHistory.setCreateTime(DateUtils.addDays(new Date(), -60));
//            passwordHistory.setExpirationTime(DateUtils.addDays(new Date(), -3));
//            user.getPasswordHistory().add(passwordHistory);
//            return user;            
//        } else if (userName.startsWith("l")) {
//            User user = new User();
//            user.setUsername(userName);
//            user.setFirstName("Larry");
//            user.setLastName("Lockout");
//            user.setLockedOutFlag(true);
//            PasswordHistory passwordHistory = new PasswordHistory();
//            passwordHistory.setCreateTime(DateUtils.addDays(new Date(), -60));
//            passwordHistory.setExpirationTime(DateUtils.addDays(new Date(), -3));
//            user.getPasswordHistory().add(passwordHistory);
//            return user;            
//        }
//        else {
//            return null;
//        }    
    }
//    
//    public User findUser(String userName) {        
//        if (userName.startsWith("u")) {
//           User user = new User();
//           user.setUsername(userName);
//           user.setFirstName("Uwe");
//           user.setLastName("Unger");
//           return user;
//        } else if (userName.startsWith("p")) {
//            User user = new User();
//            user.setUsername(userName);
//            user.setFirstName("Peter");
//            user.setLastName("Parker");
//            PasswordHistory passwordHistory = new PasswordHistory();
//            passwordHistory.setCreateTime(DateUtils.addDays(new Date(), -60));
//            passwordHistory.setExpirationTime(DateUtils.addDays(new Date(), 3));
//            user.getPasswordHistory().add(passwordHistory);
//            return user;            
//        } else if (userName.startsWith("e")) {
//            User user = new User();
//            user.setUsername(userName);
//            user.setFirstName("Earlang");
//            user.setLastName("Estefon");
//            user.setPasswordExpiredFlag(true);
//            PasswordHistory passwordHistory = new PasswordHistory();
//            passwordHistory.setCreateTime(DateUtils.addDays(new Date(), -60));
//            passwordHistory.setExpirationTime(DateUtils.addDays(new Date(), -3));
//            user.getPasswordHistory().add(passwordHistory);
//            return user;            
//        } else if (userName.startsWith("l")) {
//            User user = new User();
//            user.setUsername(userName);
//            user.setFirstName("Larry");
//            user.setLastName("Lockout");
//            user.setLockedOutFlag(true);
//            PasswordHistory passwordHistory = new PasswordHistory();
//            passwordHistory.setCreateTime(DateUtils.addDays(new Date(), -60));
//            passwordHistory.setExpirationTime(DateUtils.addDays(new Date(), -3));
//            user.getPasswordHistory().add(passwordHistory);
//            return user;            
//        }
//        else {
//            return null;
//        }
//    }
//    
    
    public void save(User user) {
        DBSession db = dbFactory.createDbSession();
        db.save(user);
        
        for (PasswordHistory passwordHistory : user.getPasswordHistory()) {
            passwordHistory.setUsername(user.getUsername());
        }
        
        db.saveAll(user.getPasswordHistory());
    }
}