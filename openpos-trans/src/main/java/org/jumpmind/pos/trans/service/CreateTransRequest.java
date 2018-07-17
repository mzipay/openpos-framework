package org.jumpmind.pos.trans.service;

import java.io.Serializable;

public class CreateTransRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String businessDate;

    private String deviceId;

    private String businessUnitId;

    private String transType;
    
    private String username;

    public CreateTransRequest() {
    }

    public CreateTransRequest(String businessDate, String deviceId, String businessUnitId, String transType, String username) {
        this.businessDate = businessDate;
        this.deviceId = deviceId;
        this.businessUnitId = businessUnitId;
        this.transType = transType;
        this.username = username;
    }

    public String getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(String businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getUsername() {
        return username;
    }

}
