package org.jumpmind.pos.user.service;

import org.apache.commons.lang3.StringUtils;
import org.jumpmind.pos.service.Endpoint;
import org.jumpmind.pos.service.ServiceResultImpl;
import org.jumpmind.pos.user.model.User;
import org.jumpmind.pos.user.model.UserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class ChangePasswordEndpoint {
    
    @Autowired
    private UserStore userStore;
    @Autowired
    private UserHelper userHelper;

    @Endpoint("/changePassword")
    public ServiceResultImpl changePassword(
            @RequestParam(value="username", defaultValue="") String username,
            @RequestParam(value="oldPassword", defaultValue="") String oldPassword,
            @RequestParam(value="newPassword1", defaultValue="") String newPassword1,
            @RequestParam(value="newPassword2", defaultValue="") String newPassword2) {
        
        User user = userStore.findUser(username);

        if (user != null) {
            ServiceResultImpl serviceResult = new ServiceResultImpl();
            serviceResult.setResultStatus("FAILURE");
            
            String currentPassword = userHelper.getCurrentPassword(user);

            // TODO check existing password. Support password hashed.
//            if (!StringUtils.equals(oldPassword, currentPassword)) {
//                serviceResult.setResultMessage("Invalid old password.");
//            } 
            if (newPassword1.length() <= 3 ) {                
                serviceResult.setResultMessage("Password must be at least 4 characters long.");
            } else if (!StringUtils.equals(newPassword1, newPassword2)) {
                serviceResult.setResultMessage("New password entry needs to match first and second try.");
            } else {
                serviceResult.setResultStatus("SUCCESS");    
            }
            
            return serviceResult;
        }
        else  {
            ServiceResultImpl result = new ServiceResultImpl();
            result.setResultStatus("GENERAL_FAILURE");
            result.setResultMessage("User not found.");
            return result;
        }        


    }    
}
