package org.jumpmind.pos.server.service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jumpmind.pos.server.config.MessageUtils;
import org.jumpmind.pos.util.DefaultObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

@Component("serverCoreSessionConnectListener")
public class SessionConnectListener implements ApplicationListener<SessionConnectEvent>, MessageUtils {

    Logger logger = LoggerFactory.getLogger(getClass());

    Map<String, Boolean> sessionAuthenticated = Collections.synchronizedMap(new HashMap<>());

    Map<String, Boolean> sessionCompatible = Collections.synchronizedMap(new HashMap<>());

    Map<String, String> sessionDeviceTypes = Collections.synchronizedMap(new HashMap<>());

    Map<String, String> sessionBrandIds = Collections.synchronizedMap(new HashMap<>());
    
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
        String deviceType = getHeader(event.getMessage(), DEVICE_TYPE_HEADER);
        String brandId = getHeader(event.getMessage(), BRAND_ID_HEADER);
        String queryParams = getHeader(event.getMessage(), QUERY_PARAMS_HEADER);
        sessionQueryParamsMap.put(sessionId, toQueryParams(queryParams));
        sessionAuthenticated.put(sessionId, serverAuthToken == null || serverAuthToken.equals(authToken));
        sessionCompatible.put(sessionId, serverCompatibilityVersion == null || serverCompatibilityVersion.equals(compatibilityVersion));
        sessionDeviceTypes.put(sessionId, deviceType);
        sessionBrandIds.put(sessionId, brandId);
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

    public boolean isSessionAuthenticated(String sessionId) {
        return this.sessionAuthenticated.get(sessionId) != null && this.sessionAuthenticated.get(sessionId);
    }

    public boolean isSessionCompatible(String sessionId) {
        return this.sessionCompatible.get(sessionId) != null && this.sessionCompatible.get(sessionId);
    }

    public String getDeviceType(String sessionId) {
        return sessionDeviceTypes.get(sessionId);
    }

    public String getBrandId(String sessionId) {
        return sessionBrandIds.get(sessionId);
    }
    
    public Map<String, Object> getQueryParams(String sessionId) {
        return sessionQueryParamsMap.get(sessionId);
    }

    public void removeSession(String sessionId) {
        this.sessionAuthenticated.remove(sessionId);
        this.sessionCompatible.remove(sessionId);
    }

}