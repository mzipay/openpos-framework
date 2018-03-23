package org.jumpmind.pos.user.service;

import org.jumpmind.pos.service.EndpointDispatcher;
import org.jumpmind.pos.service.ServiceResultImpl;
import org.jumpmind.pos.user.model.AuthenticationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserService {
    
    @Autowired
    private EndpointDispatcher endpointDispatcher;
    
    @RequestMapping("/authenticate")
    public AuthenticationResult authenticate(
            @RequestParam(value="username", defaultValue="") String username,
            @RequestParam(value="password", defaultValue="") String password) {
        return endpointDispatcher.dispatch("/authenticate", username, password);
    }
    
    @RequestMapping("/changePassword")
    public ServiceResultImpl changePassword(
            @RequestParam(value="username", defaultValue="") String username,
            @RequestParam(value="oldPassword", defaultValue="") String oldPassword,
            @RequestParam(value="newPassword1", defaultValue="") String newPassword1,
            @RequestParam(value="newPassword2", defaultValue="") String newPassword2) {
        return endpointDispatcher.dispatch("/changePassword", username, oldPassword, newPassword1, newPassword2);
    }  
}
