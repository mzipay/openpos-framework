package org.jumpmind.pos.ops.model;

import java.io.Serializable;
import java.util.Date;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Entity;
import org.jumpmind.pos.persist.Table;
import org.jumpmind.pos.trans.model.TransType;

@Table(name = "unit_status")
public class UnitStatusModel extends Entity implements Serializable {

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

    @Column(size = "10")
    private String businessDate;

    public UnitStatusModel(String unitId, String unitType, String unitStatus, String businessDate, Date createTime) {
        super();
        this.unitId = unitId;
        this.unitType = unitType;
        this.unitStatus = unitStatus;
        this.businessDate = businessDate;
        this.setCreateTime(createTime);
    }

    public UnitStatusModel() {
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

    public TransType toTransactionType() {
        if (unitStatus != null && unitType != null) {
            if (unitStatus.equals(UnitStatus.OPEN.name())) {
                if (unitType.equals(UnitType.DEVICE.name())) {
                    return TransType.OPEN_DEVICE;
                } else if (unitType.equals(UnitType.STORE.name())) {
                    return TransType.OPEN_BUSINESS_UNIT;
                }
            } else if (unitStatus.equals(UnitStatus.CLOSED.name())) {
                if (unitType.equals(UnitType.DEVICE.name())) {
                    return TransType.CLOSE_DEVICE;
                } else if (unitType.equals(UnitType.STORE.name())) {
                    return TransType.CLOSE_BUSINESS_UNIT;
                }
            }
        }
        return null;
    }

}
