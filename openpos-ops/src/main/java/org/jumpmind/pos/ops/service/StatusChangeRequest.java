package org.jumpmind.pos.ops.service;

import java.io.Serializable;
import java.util.Date;

public class StatusChangeRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    String unitType;
    String unitId;
    String newStatus;
    String requestingDeviceId;
    String businessDate;
    String businessUnitId;
    String username;
    Date timeOfRequest = new Date();

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public String getRequestingDeviceId() {
        return requestingDeviceId;
    }

    public void setRequestingDeviceId(String requestingDeviceId) {
        this.requestingDeviceId = requestingDeviceId;
    }

    public String getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(String businessDay) {
        this.businessDate = businessDay;
    }
    
    public void setBusinessUnitId(String businessUnitId) {
        this.businessUnitId = businessUnitId;
    }
   
    public String getBusinessUnitId() {
        return businessUnitId;
    }
    
    public void setTimeOfRequest(Date dateOfRequest) {
        this.timeOfRequest = dateOfRequest;
    }
    
    public Date getTimeOfRequest() {
        return timeOfRequest;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getUsername() {
        return username;
    }

}
