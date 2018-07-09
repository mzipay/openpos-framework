package org.jumpmind.pos.ops.model;

import java.io.Serializable;
import java.util.Date;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Entity;
import org.jumpmind.pos.persist.Table;

@Table
public class UnitStatus extends Entity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Column(primaryKey = true)
    private Long sequenceNumber;
    
    @Column(primaryKey = true)
    private String businessUnitId;
    
    @Column(primaryKey = true)
    private String deviceId;
    
    @Column
    private String unitId;
    
    @Column
    private String unitType;
    
    @Column
    private String unitStatus;
    
    @Column(size="10")
    private String businessDate;
    
    public UnitStatus(String unitId, String unitType, String unitStatus, String businessDate, Date createTime) {
        super();
        this.unitId = unitId;
        this.unitType = unitType;
        this.unitStatus = unitStatus;
        this.businessDate = businessDate;
        this.setCreateTime(createTime);
    }

    public UnitStatus() {
    }
    
    public void setUnitId(String entityId) {
        this.unitId = entityId;
    }
    
    public String getUnitId() {
        return unitId;
    }    

    public void setUnitStatus(String status) {
        this.unitStatus = status;
    }
    
    public String getUnitStatus() {
        return unitStatus;
    }
    
    public void setUnitType(String entityType) {
        this.unitType = entityType;
    }
    
    public String getUnitType() {
        return unitType;
    }

    public Long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(String businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate;
    }
    
    
      

}
