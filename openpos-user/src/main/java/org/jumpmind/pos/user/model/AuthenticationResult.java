package org.jumpmind.pos.user.model;

import org.jumpmind.pos.login.model.User;
import org.jumpmind.pos.service.ServiceResultImpl;

public class AuthenticationResult extends ServiceResultImpl {
    
    private String authenticationCode;
    private User user;

    public AuthenticationResult() {
        
    }
    
    public AuthenticationResult(String resultStatus) {
        setResultStatus(resultStatus);
    }
    public AuthenticationResult(String resultStatus, User user) {
        setResultStatus(resultStatus);
        this.user = user;
    }

    public String getAuthenticationCode() {
        return authenticationCode;
    }
    
    public void setAuthenticationCode(String authenticationCode) {
        this.authenticationCode = authenticationCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
