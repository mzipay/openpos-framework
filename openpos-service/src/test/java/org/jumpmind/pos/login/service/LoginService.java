package org.jumpmind.pos.login.service;

import org.jumpmind.pos.login.model.AuthenticationResult;
import org.jumpmind.pos.service.EndpointDispatcher;
import org.jumpmind.pos.service.ServiceResultImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginService {
    
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
            @RequestParam(value="newPassword", defaultValue="") String newPassword) {
        return endpointDispatcher.dispatch("/changePassword", username, oldPassword, newPassword);
    }    

}
