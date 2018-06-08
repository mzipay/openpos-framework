package org.jumpmind.pos.user.service;

import java.util.Date;

import org.apache.commons.collections4.CollectionUtils;
import org.jumpmind.pos.cache.service.CacheContainer;
import org.jumpmind.pos.cache.service.impl.ICache;
import org.jumpmind.pos.context.service.ContextServiceClient;
import org.jumpmind.pos.service.Endpoint;
import org.jumpmind.pos.service.In;
import org.jumpmind.pos.user.model.PasswordHistory;
import org.jumpmind.pos.user.model.User;
import org.jumpmind.pos.user.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@Scope("prototype")
public class AuthenticateEndpoint {

    @Autowired
    private UserRepository userRepository;
    @In
    private ContextServiceClient contextServiceClient;
//    @In
//    private ICache userCache;

    @Endpoint("/authenticate")
    public AuthenticationResult authenticate(
            @RequestParam(value="deviceId", defaultValue="") String deviceId,
            @RequestParam(value="locale", defaultValue="") String locale,
            @RequestParam(value="username", defaultValue="") String username,
            @RequestParam(value="password", defaultValue="") String password) {
        
//        User user = userCache.getOrLoad(username, k -> {
//                return userRepository.findUser(username);
//            }
//        );
        
        User user = userRepository.findUser(username);

        if (user != null) {
            if (!checkPassword(user, password)) {
                return failAuthentication(user, "Incorrect Password", "PASSWORD_INCORRECT");
            } else if (checkLockout(user)) {
                return failAuthentication(user, "User locked out.", "USER_LOCKOUT");
            } else if (user.isPasswordExpiredFlag()) {
                return handlePasswordExpired(user);
            } else {
                return handleSuccess(user);
            }
        }
        else  {
            return failAuthentication(user, "User not found.", "GENERAL_FAILURE");              
        }
    }

    private AuthenticationResult handleSuccess(User user) {
        AuthenticationResult result =  new AuthenticationResult("SUCCESS", user);
        long expiresInDays = passwordExpiresInDays(user);
        if (expiresInDays > 0 
                && expiresInDays <= contextServiceClient.getInt("openpos.user.warn.password.expires.days")) {
            UserMessage message = new UserMessage();
            message.setMessageCode("PASSWORD_EXPIRY_WARNING");
            message.setMessage(String.format("Your password exires in %s days.", expiresInDays));
            result.getUserMessages().add(message);
            
        }
        return result;
    }

    protected AuthenticationResult handlePasswordExpired(User user) {
        UserMessage message = new UserMessage();
        long expiresInDays = Math.abs(passwordExpiresInDays(user));
        message.setMessageCode("PASSWORD_EXPIRED");
        if (expiresInDays > 0) {                    
            message.setMessage(String.format("Your password expired %s day(s) ago.", expiresInDays));                
        } else {
            message.setMessage(String.format("Your password has expired.", expiresInDays));
        }
        AuthenticationResult result = failAuthentication(user, "Password expired.", "PASSWORD_EXPIRED");
        result.getUserMessages().add(message);
        return result;
    }

    protected boolean checkPassword(User user, String password) {
        if (password.length() < 3) {
            user.setPasswordFailedAttempts(user.getPasswordFailedAttempts()+1);
            user.setLastPasswordAttempt(new Date());
            if (user.getPasswordFailedAttempts() > contextServiceClient.getInt("openpos.user.max.login.attempts")) {
                user.setLockedOutFlag(true);
            }
            userRepository.save(user);
            return false;
        }
        return true;
    }

    protected boolean checkLockout(User user) {
        if (user.isLockedOutFlag()) {
            Date lastPasswordAttempt = user.getLastPasswordAttempt();
            if (lastPasswordAttempt != null) {      
                Date passwordAttemptsResetDate = new Date(lastPasswordAttempt.getTime() + 
                        contextServiceClient.getLong("openpos.user.attempts.reset.period.ms"));
                Date now = new Date();
                if (now.after(passwordAttemptsResetDate)) {                    
                    user.setLockedOutFlag(false);
                    user.setPasswordFailedAttempts(0);
                    userRepository.save(user);
                }
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
    
    protected long passwordExpiresInDays(User user) {
        if (CollectionUtils.isEmpty(user.getPasswordHistory())) {
            return -1;
        }
        
        PasswordHistory passwordHistory = user.getPasswordHistory().get(0);
        if (passwordHistory.getExpirationTime() != null) {
            Date now = new Date(); // TODO the server may not be the same timezone as client.
            long diffDays = org.jumpmind.pos.service.util.DateUtils.daysBetween(now, passwordHistory.getExpirationTime());
            return diffDays;
        }
        
        return -1;
    }

}
