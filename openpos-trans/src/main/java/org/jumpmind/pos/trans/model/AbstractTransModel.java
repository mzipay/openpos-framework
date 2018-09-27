package org.jumpmind.pos.trans.model;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Entity;

abstract public class AbstractTransModel extends Entity {

    private static final long serialVersionUID = 1L;

    @Column(primaryKey = true)
    private Long sequenceNumber;
    
    @Column(primaryKey = true, size="10")
    private String businessDate;
    
    @Column(primaryKey = true)
    private String deviceId;
    
    
    public void setSequenceNumber(Long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
    
    public Long getSequenceNumber() {
        return sequenceNumber;
    }
    
    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate;
    }
    
    public String getBusinessDate() {
        return businessDate;
    }
    
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    
    public String getDeviceId() {
        return deviceId;
    }
    
}
