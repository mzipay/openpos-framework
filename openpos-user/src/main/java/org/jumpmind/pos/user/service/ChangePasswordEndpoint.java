package org.jumpmind.pos.user.service;

import org.jumpmind.pos.service.Endpoint;
import org.jumpmind.pos.service.ServiceResultImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class ChangePasswordEndpoint {

    @Endpoint("/changePassword")
    public ServiceResultImpl changePassword(
            @RequestParam(value="username", defaultValue="") String username,
            @RequestParam(value="oldPassword", defaultValue="") String oldPassword,
            @RequestParam(value="newPassword", defaultValue="") String newPassword) {

        System.out.println("Processing change password service.");
        ServiceResultImpl serviceResult = new ServiceResultImpl();
        serviceResult.setResultStatus("FAILURE");
        serviceResult.setResultMessage("Not implemented.");
        return serviceResult;

    }    
}
