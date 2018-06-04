package org.jumpmind.pos.context.model;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.jumpmind.pos.context.ContextException;
import org.jumpmind.pos.persist.DBSession;
import org.jumpmind.pos.persist.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

@Repository
@DependsOn(value = { "ContextModule" })
public class ContextRepository {

    private static Logger log = LoggerFactory.getLogger(ContextRepository.class);

    private Query<ConfigModel> configLookup = new Query<ConfigModel>()
            .named("configLookup")
            .result(ConfigModel.class);
    private Query<DeviceModel> devicesByTag = new Query<DeviceModel>()
            .named("devicesByTag")
            .result(DeviceModel.class);
    
    private static TagConfig tagConfig;

    @Autowired
    @Lazy
    DBSession contextSession;
    
    public DeviceModel findDevice(String deviceId) {
        DeviceModel device = contextSession.findByNaturalId(DeviceModel.class, deviceId);
        if (device != null) {            
            addTags(device, device.getAdditionalFields());
        }
        return device;
    }

    public List<DeviceModel> findDevicesByTag(String tagName, String tagValue) {
        Map<String, Object> params = new HashMap<>();
        String tagColumnName = TagModel.TAG_PREFIX + tagName;
        params.put("tagColumnName", tagColumnName);
        params.put("tagValue", tagValue);
        List<DeviceModel> devices = contextSession.query(devicesByTag, params);
        addTagsToDevices(devices);
        return devices;
    }    

    public List<ConfigModel> findConfigs(Date currentTime, String configName) {
        Map<String, Object> params = new HashMap<>();
        params.put("configName", configName);
        params.put("currentTime", currentTime);
        
        List<ConfigModel> configs = contextSession.query(configLookup, params);
        if (configs != null && !CollectionUtils.isEmpty(configs)) {
            addTagsToConfigs(configs);
            return configs;
        } else {
            if (log.isDebugEnabled()) {                
                log.debug("No configuration found for " + configName);
            }
            return null;
        }
    }
    
    protected void addTagsToConfigs(List<ConfigModel> configs) {
        if (configs != null) {
            for (ConfigModel config : configs) {
                addTags(config, config.getAdditionalFields());
            }
        }        
    }    
    
    protected void addTagsToDevices(List<DeviceModel> devices) {
        if (devices != null) {
            for (DeviceModel device : devices) {
                addTags(device, device.getAdditionalFields());
            }
        }        
    }    
    
    protected void addTags(ITaggedElement taggedElement, Map<String, Object> fields) {
        if (taggedElement != null) {
            Map<String, String> tags = additionalFieldsToTags(fields);
            taggedElement.setTags(tags);
        }        
    }
    
    public static TagConfig getTagConfig() {
        if (tagConfig == null) {            
            try {
                URL url = Thread.currentThread().getContextClassLoader().getResource("openpos-tags.yaml");
                if (url != null) {
                    log.info(String.format("Loading %s...", url.toString()));
                    InputStream queryYamlStream = url.openStream();
                    tagConfig = new Yaml(new Constructor(TagConfig.class)).load(queryYamlStream);
                    return tagConfig;
                } else {
                    log.info("Could not locate tags.yaml on the classpath.");
                    tagConfig = new TagConfig();
                    return tagConfig;
                }
            } catch (Exception ex) {
                throw new ContextException("Failed to load tags.yaml", ex);
            }
        } else {
            return tagConfig;
        }
    }
    
    protected Map<String, String> additionalFieldsToTags(Map<String, Object> additionalFields) {
        Map<String, String> tags = new HashMap<>();
        for (String columnName : additionalFields.keySet()) {
            String columnUpper = columnName.toUpperCase();
            if (columnUpper.startsWith(TagModel.TAG_PREFIX)) {
                Object value = additionalFields.get(columnName);
                String tagName = columnUpper.substring(TagModel.TAG_PREFIX.length());
                tags.put(tagName, value.toString());
            }
        }
        
        return tags;
    }
 
}
