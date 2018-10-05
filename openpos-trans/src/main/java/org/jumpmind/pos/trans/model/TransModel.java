package org.jumpmind.pos.trans.model;

import java.util.Date;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Table;

@Table(name="trans")
public class TransModel extends AbstractTransModel {

    private static final long serialVersionUID = 1L;

    @Column
    private String transType;

    @Column
    private TransStatus transStatus;

    @Column
    private String businessUnitId;

    @Column
    private String username;

    @Column
    private Date beginTime;

    @Column
    private Date endTime;      
    
    @Column
    private boolean keyedOffline;

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public TransStatus getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(TransStatus transStatus) {
        this.transStatus = transStatus;
    }

    public String getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(String businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String operatorUsername) {
        this.username = operatorUsername;
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

    public boolean isKeyedOffline() {
        return keyedOffline;
    }

    public void setKeyedOffline(boolean keyedOffline) {
        this.keyedOffline = keyedOffline;
    }

    
}
