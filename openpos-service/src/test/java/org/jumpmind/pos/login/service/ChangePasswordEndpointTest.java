package org.jumpmind.pos.login.service;

import org.jumpmind.pos.service.ServiceResultImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class ChangePasswordEndpointTest {

    @RequestMapping("/changePassword")
    public ServiceResultImpl changePassword(
            @RequestParam(value="username", defaultValue="") String username,
            @RequestParam(value="oldPassword", defaultValue="") String oldPassword,
            @RequestParam(value="newPassword", defaultValue="") String newPassword) {

        System.out.println("Processing authentication service.");
        ServiceResultImpl serviceResult = new ServiceResultImpl();
        serviceResult.setResultStatus("FAILURE");
        serviceResult.setResultMessage("Not implemented.");
        return serviceResult;

    }    
}
