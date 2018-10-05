package org.jumpmind.pos.user.service;

import java.util.ArrayList;
import java.util.List;

import org.jumpmind.pos.service.ServiceResult;
import org.jumpmind.pos.user.model.UserModel;

public class AuthenticationResult extends ServiceResult {
    
    private static final long serialVersionUID = 1L;
    private String authenticationCode;
    private UserModel user;
    private List<UserMessage> userMessages = new ArrayList<>();

    public AuthenticationResult() {
        
    }
    
    public AuthenticationResult(String resultStatus) {
        setResultStatus(resultStatus);
    }
    public AuthenticationResult(String resultStatus, UserModel user) {
        setResultStatus(resultStatus);
        this.user = user;
    }

    public String getAuthenticationCode() {
        return authenticationCode;
    }
    
    public void setAuthenticationCode(String authenticationCode) {
        this.authenticationCode = authenticationCode;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public List<UserMessage> getUserMessages() {
        return userMessages;
    }

    public void setUserMessages(List<UserMessage> userMessages) {
        this.userMessages = userMessages;
    }

}
