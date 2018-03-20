package org.jumpmind.pos.login.model;

import org.springframework.stereotype.Component;

@Component
public class UserStore {
    
    public User findUser(String userName) {        
        if (userName.startsWith("j")) {
           User user = new User();
           user.setUserName(userName);
           user.setFirstName("Fred");
           user.setLastName("Jones");
           return user;
        } else {
            return null;
        }
    }
    
    
    public void save(User user) {
        // TODO
    }
}
