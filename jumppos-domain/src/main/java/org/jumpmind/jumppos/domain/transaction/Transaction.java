package org.jumpmind.jumppos.domain.transaction;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

import org.jumpmind.jumppos.domain.BaseEntity;
import org.jumpmind.jumppos.domain.security.Operator;

/**
 * A record of business activity that involves a financial and/or merchandise
 * unit exchange or the granting of access to conduct business at a specific
 * device, at a specific point in time for a specific employee. A record of
 * activity performed by an Operator on a Workstation.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract public class Transaction extends BaseEntity {

    @Id
    private String id;

    @OneToOne
    private Operator operator;

    private String businessDate;

    private int sequenceNumber;

    private String workstationId;

    private String businessUnitId;

    private Date beginTime = new Date();
    
    private Date endTime;

    private Boolean trainingFlag;

    private Boolean keyedOfflineFlag;
    
    private Boolean cancelledFlag;
    
    private Boolean voidedFlag;
    
    private Boolean suspendedFlag;

    public Transaction() {

    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public String getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate;
    }

    public void setWorkstationId(String workstationId) {
        this.workstationId = workstationId;
    }

    public String getWorkstationId() {
        return workstationId;
    }

    public void setBusinessUnitId(String retailStoreId) {
        this.businessUnitId = retailStoreId;
    }

    public String getBusinessUnitId() {
        return businessUnitId;
    }

    public Boolean getTrainingFlag() {
        return trainingFlag;
    }

    public void setTrainingFlag(Boolean trainingFlag) {
        this.trainingFlag = trainingFlag;
    }

    public Boolean getKeyedOfflineFlag() {
        return keyedOfflineFlag;
    }

    public void setKeyedOfflineFlag(Boolean keyedOfflineFlag) {
        this.keyedOfflineFlag = keyedOfflineFlag;
    }

    public Boolean getCancelledFlag() {
        return cancelledFlag;
    }

    public void setCancelledFlag(Boolean cancelledFlag) {
        this.cancelledFlag = cancelledFlag;
    }

    public Boolean getVoidedFlag() {
        return voidedFlag;
    }

    public void setVoidedFlag(Boolean voidedFlag) {
        this.voidedFlag = voidedFlag;
    }

    public Boolean getSuspendedFlag() {
        return suspendedFlag;
    }

    public void setSuspendedFlag(Boolean suspendedFlag) {
        this.suspendedFlag = suspendedFlag;
    }

    
}
