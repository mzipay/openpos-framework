package org.jumpmind.pos.context.service;

import java.util.Date;

import org.jumpmind.pos.service.PosServerException;
import org.jumpmind.pos.service.util.DateUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ContextServiceClient {

    private String nodeId;
    private ContextService contextService;
    private ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private Date configDate;

    public ContextServiceClient(ContextService contextService, String nodeId) {
        this.nodeId = nodeId;
        this.contextService = contextService;
    }

    public Date getDate(String configName) {
        return DateUtils.parseDateTimeISO(getConfigValue(configName));
    }

    public int getInt(String configName) {
        try {            
            return Integer.valueOf(getConfigValue(configName));
        }
        catch (Exception ex) {
            throw new PosServerException("Failed to convert " + configName + "='" + getConfigValue(configName) + "' to an int.'");
        }
    }

    public long getLong(String configName) {
        try {
            return Long.valueOf(getConfigValue(configName));
        }
        catch (Exception ex) {
            throw new PosServerException("Failed to convert '" + getConfigValue(configName) + "' to a long.'");
        }        
    }

    public String getString(String configName) {
        return getConfigValue(configName);
    }

    public <T> T getObject(String configName, Class<T> argType) {
        String value = getConfigValue(configName);
        try {
            return mapper.readValue(value, argType);
        } catch (Exception ex) {
            throw new PosServerException("Failed to parse config json '" + value + "' as " + argType, ex);
        }
    }    

    protected String getConfigValue(String configName) {
        ConfigResult configResult = contextService.getConfig(nodeId, getConfigDate(), configName);
        if (configResult.isSuccess()) {
            return configResult.getConfigValue();
        } else {
            // TODO consider loading default values here.
            throw new PosServerException("No config value and no default found for configName '" + configName + "'");
        }
    }

    protected Date getConfigDate() {
        if (this.configDate == null) {
            return new Date();
        } else {
            return configDate;
        }
    }

    public void setConfigDate(Date configDate) {
        this.configDate = configDate;
    }
}
