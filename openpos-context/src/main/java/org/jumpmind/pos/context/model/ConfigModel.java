package org.jumpmind.pos.context.model;

import java.sql.Types;
import java.util.Date;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Entity;
import org.jumpmind.pos.persist.Table;

@Table(name="config")
public class ConfigModel extends Entity {
    
    public static final String TAG_ALL = "*";

    @Column(size="50")
    private String locationType;
    @Column(size="50", primaryKey=true) 
    private String locationValue;    
    @Column(primaryKey=true)
    private String storeType = TAG_ALL;
    @Column(primaryKey=true)
    private String departmentId = TAG_ALL;
    @Column(primaryKey=true)
    private String brandId = TAG_ALL;
    @Column(primaryKey=true)
    private String deviceType = TAG_ALL;
    @Column(primaryKey=true)
    private Date effectiveDate;
    @Column(primaryKey=true)
    private String configName;
    @Column
    private Date expirationDate;
    @Column
    private boolean enabledFlag;
    // The value is a JSON object.
    @Column(type=Types.CLOB)
    private String configValue;
 
    public LocationType getLocationType() {
        if (locationType != null) {            
            return LocationType.valueOf(locationType);
        } else {
            return null;
        }
    }
    
    public void setLocationType(LocationType locationType) {
        if (locationType == null) {
            this.locationType = null;
        } else {            
            this.locationType = locationType.name();
        }
    }
    public String getLocationValue() {
        return locationValue;
    }
    public void setLocationValue(String locationValue) {
        this.locationValue = locationValue;
    }
    public String getStoreType() {
        return storeType;
    }
    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }
    public String getDepartmentId() {
        return departmentId;
    }
    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
    public String getBrandId() {
        return brandId;
    }
    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }
    public String getDeviceType() {
        return deviceType;
    }
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
    public Date getEffectiveDate() {
        return effectiveDate;
    }
    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
    public Date getExpirationDate() {
        return expirationDate;
    }
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
    public String getConfigName() {
        return configName;
    }
    public void setConfigName(String configName) {
        this.configName = configName;
    }
    public String getConfigValue() {
        return configValue;
    }
    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public boolean isEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(boolean enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }
}
