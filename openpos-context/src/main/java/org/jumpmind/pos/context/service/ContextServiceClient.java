package org.jumpmind.pos.context.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.jumpmind.pos.service.PosServerException;
import org.jumpmind.pos.service.util.DateUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ContextServiceClient {

    private String devuceId;
    private ContextService contextService;
    private ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private Date configDate;

    public ContextServiceClient(ContextService contextService, String deviceId) {
        this.devuceId = deviceId;
        this.contextService = contextService;
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        dateFormat.setTimeZone(TimeZone.getDefault());
        mapper.setDateFormat(dateFormat);
        
        SimpleModule module = new SimpleModule();
        module.addDeserializer(BigDecimal.class, new BigDecimalDeserializer());
        mapper.registerModule(module);
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
    
    public <T> List<T> getObjectList(String configName, Class<T> argType) {
        String value = getConfigValue(configName);
        try {
            return mapper.readValue(value, 
                    mapper.getTypeFactory().constructCollectionType(List.class, argType));
        } catch (Exception ex) {
            throw new PosServerException("Failed to parse config json '" + value + "' as a list of " + argType, ex);
        }        
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
        ConfigResult configResult = contextService.getConfig(devuceId, getConfigDate(), configName);
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
