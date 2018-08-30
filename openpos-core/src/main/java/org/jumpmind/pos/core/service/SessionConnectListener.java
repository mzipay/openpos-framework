package org.jumpmind.pos.core.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jumpmind.pos.core.config.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

@Component
public class SessionConnectListener implements ApplicationListener<SessionConnectEvent>, MessageUtils {

    Logger logger = LoggerFactory.getLogger(getClass());
        
    Map<String, Boolean> sessionAuthenticated = Collections.synchronizedMap(new HashMap<>());
    
    Map<String, Boolean> sessionCompatible = Collections.synchronizedMap(new HashMap<>());
    
    @Value("${openpos.auth.token:#{null}}")
    String serverAuthToken;  
    
    @Value("${openpos.compatibility.version:#{null}}")
    String serverCompatibilityVersion; 

    public void onApplicationEvent(SessionConnectEvent event) {
        String sessionId = (String) event.getMessage().getHeaders().get("simpSessionId");
        logger.info("session connected: {}", sessionId);
        String authToken = getHeader(event.getMessage(), "authToken");
        String compatibilityVersion = getHeader(event.getMessage(), MessageUtils.COMPATIBILITY_VERSION_HEADER);
        sessionAuthenticated.put(sessionId, serverAuthToken == null || serverAuthToken.equals(authToken));
        sessionCompatible.put(sessionId, serverCompatibilityVersion == null || serverCompatibilityVersion.equals(compatibilityVersion));
    }
    
    protected boolean isSessionAuthenticated(String sessionId) {        
        return this.sessionAuthenticated.get(sessionId) != null && this.sessionAuthenticated.get(sessionId);
    }
    
    protected boolean isSessionCompatible(String sessionId) {        
        return this.sessionCompatible.get(sessionId) != null && this.sessionCompatible.get(sessionId);
    }
    
    protected void removeSession(String sessionId) {
        this.sessionAuthenticated.remove(sessionId);
        this.sessionCompatible.remove(sessionId);
    }


}