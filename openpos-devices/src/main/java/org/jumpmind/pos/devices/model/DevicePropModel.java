package org.jumpmind.pos.devices.model;

import org.jumpmind.pos.persist.AbstractModel;
import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Table;

@Table(name="device_prop")
public class DevicePropModel extends AbstractModel {

    private static final long serialVersionUID = 1L;

    @Column(primaryKey=true)
    String profile;
    
    @Column(primaryKey=true)
    String deviceName;
    
    @Column(primaryKey=true)
    String propertyName;
    
    @Column
    String propertyValue;
    
    @Column
    String propertyType;

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }
    
    public String getPropertyType() {
        return propertyType;
    }
    
}
