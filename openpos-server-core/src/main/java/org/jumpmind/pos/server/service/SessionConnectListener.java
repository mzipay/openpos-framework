package org.jumpmind.pos.server.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jumpmind.pos.server.config.MessageUtils;
import org.jumpmind.pos.util.DefaultObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import com.fasterxml.jackson.core.type.TypeReference;

@Component("serverCoreSessionConnectListener")
public class SessionConnectListener implements ApplicationListener<SessionConnectEvent>, MessageUtils {

    Logger logger = LoggerFactory.getLogger(getClass());

    Map<String, Boolean> sessionAuthenticated = Collections.synchronizedMap(new HashMap<>());

    Map<String, Boolean> sessionCompatible = Collections.synchronizedMap(new HashMap<>());

    Map<String, List<String>> sessionPersonalizationResults = Collections.synchronizedMap(new HashMap<>());
    
    Map<String, Map<String, Object>> sessionQueryParamsMap = Collections.synchronizedMap(new HashMap<>());

    @Value("${openpos.auth.token:#{null}}")
    String serverAuthToken;

    @Value("${openpos.compatibility.version:#{null}}")
    String serverCompatibilityVersion;

    public void onApplicationEvent(SessionConnectEvent event) {
        String sessionId = (String) event.getMessage().getHeaders().get("simpSessionId");
        logger.info("session connected: {}", sessionId);
        String authToken = getHeader(event.getMessage(), "authToken");
        String compatibilityVersion = getHeader(event.getMessage(), COMPATIBILITY_VERSION_HEADER);
        List<String> personalizationResults = getHeaderList(event.getMessage(), PERSONALIZATION_RESULTS_HEADER);
        String queryParams = getHeader(event.getMessage(), QUERY_PARAMS_HEADER);
        sessionQueryParamsMap.put(sessionId, toQueryParams(queryParams));
        sessionAuthenticated.put(sessionId, serverAuthToken == null || serverAuthToken.equals(authToken));
        sessionCompatible.put(sessionId, serverCompatibilityVersion == null || serverCompatibilityVersion.equals(compatibilityVersion));
        sessionPersonalizationResults.put(sessionId, personalizationResults);
    }
    
    private Map<String,Object> toQueryParams(String json) {
        TypeReference<HashMap<String, Object>> typeRef 
        = new TypeReference<HashMap<String, Object>>() {};
        try {
            return DefaultObjectMapper.build().readValue(json, typeRef);
        } catch (Exception e) {
            logger.error("Failed to parse query params", e);
            return Collections.emptyMap();
        }
    }
    
    @SuppressWarnings("unchecked")
    private List<String> getHeaderList(Message<?> message, String name) {
        List<String> values = null;
        String header = getHeader(message, name);
        if (header != null) {
            try {
                values = DefaultObjectMapper.build().readValue(header, List.class);
            } catch (Exception e) {
                logger.error("Failed to parse personalization results", e);
            }
        }
        return values;
    }

    public boolean isSessionAuthenticated(String sessionId) {
        return this.sessionAuthenticated.get(sessionId) != null && this.sessionAuthenticated.get(sessionId);
    }

    public boolean isSessionCompatible(String sessionId) {
        return this.sessionCompatible.get(sessionId) != null && this.sessionCompatible.get(sessionId);
    }

    public List<String> getPersonalizationResults(String sessionId) {
        return sessionPersonalizationResults.get(sessionId);
    }

    public Map<String, Object> getQueryParams(String sessionId) {
        return sessionQueryParamsMap.get(sessionId);
    }

    public void removeSession(String sessionId) {
        this.sessionAuthenticated.remove(sessionId);
        this.sessionCompatible.remove(sessionId);
    }

}