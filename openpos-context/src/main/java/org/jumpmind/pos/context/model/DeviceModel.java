package org.jumpmind.pos.context.model;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Entity;
import org.jumpmind.pos.persist.Table;

@Table(name = "Device")
public class DeviceModel extends Entity implements ITaggedElement {

    @Column(primaryKey = true)
    private String deviceId;
    
    @Column
    private String deviceType; // STORE/DC/WORKSTATION/HANDELD/CUSTOMER
                               // HANDHELD/WEBSITE, etc.
    @Column(size = "10")
    String locale;
    
    @Column
    String businessUnitId;
    
    @Column(size = "254")
    private String description;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private Map<String, String> tags = new LinkedHashMap<String, String>();

    @Override
    public String getTagValue(String tagName) {
        return tags.get(tagName);
    }

    @Override
    public void setTagValue(String tagName, String tagValue) {
        tags.put(tagName, tagValue);
    }

    @Override
    public void setTags(Map<String, String> tags) {
        this.tags.clear();
        this.tags.putAll(tags);
    }

    @Override
    public void clearTagValue(String tagName) {
        tags.remove(tagName);
    }

    @Override
    public Map<String, String> getTags() {
        return new LinkedHashMap<>(tags);
    }
    
    public void setBusinessUnitId(String businessUnitId) {
        this.businessUnitId = businessUnitId;
    }
    
    public String getBusinessUnitId() {
        return businessUnitId;
    }

}
