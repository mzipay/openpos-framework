package org.jumpmind.pos.login.service;

import java.util.Date;

import org.jumpmind.pos.login.model.AuthenticationResult;
import org.jumpmind.pos.login.model.User;
import org.jumpmind.pos.login.model.UserStore;
import org.jumpmind.pos.service.Endpoint;
import org.jumpmind.pos.service.config.IConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class AuthenticateEndpointTest {

    @Autowired
    UserStore userStore;
    @Autowired
    IConfigService configService;

    @Endpoint("/authenticate")
    public AuthenticationResult authenticate(
            @RequestParam(value="username", defaultValue="") String username,
            @RequestParam(value="password", defaultValue="") String password) {

        System.out.println("Processing authentication service.");

        User user = userStore.findUser(username);

        if (user != null) {
            if (!checkPassword(user, password)) {
                return failAuthentication(user, "Incorrect Password", "PASSWORD_INCORRECT");
            } else if (checkLockout(user)) {
                return failAuthentication(user, "User locked out.", "USER_LOCKOUT");
            } else if (user.isPasswordExpiredFlag()){
                return failAuthentication(user, "Password expired.", "PASSWORD_EXPIRED");
            } else {                    
                return new AuthenticationResult("SUCCESS", user);
            }
        }
        else  {
            return failAuthentication(user, "User not found.", "GENERAL_FAILURE");              
        }
    }

    protected boolean checkPassword(User user, String password) {
        if (password.length() < 3) {
            user.setPasswordFailedAttempts(user.getPasswordFailedAttempts()+1);
            user.setLastPasswordAttempt(new Date());

            if (user.getPasswordFailedAttempts() > configService.getInt("openpos.max.login.attempts")) {
                user.setLockedOutFlag(true);
            }
            userStore.save(user);
            return false;
        }
        return true;
    }

    protected boolean checkLockout(User user) {
        if (user.isLockedOutFlag()) {
            Date passwordAttemptsResetDate = new Date(System.currentTimeMillis()-configService.getLong("openpos.login.attempts.reset.period.ms"));
            if (user.getLastLogin().after(passwordAttemptsResetDate)) {
                user.setLockedOutFlag(false);
                user.setPasswordFailedAttempts(0);
                userStore.save(user);
            }
        }
        return user.isLockedOutFlag();
    }

    protected AuthenticationResult failAuthentication(User user, String message, String code) {
        AuthenticationResult result = new AuthenticationResult("FAILURE");
        result.setAuthenticationCode(code);
        result.setResultMessage(message);
        return result;       
    }

}
