package org.jumpmind.pos.trans.model;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.AbstractModel;

abstract public class AbstractTransModel extends AbstractModel {

    private static final long serialVersionUID = 1L;

    @Column(primaryKey = true)
    Long sequenceNumber;

    @Column(primaryKey = true, size = "10")
    String businessDate;

    @Column(primaryKey = true)
    String deviceId;

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
