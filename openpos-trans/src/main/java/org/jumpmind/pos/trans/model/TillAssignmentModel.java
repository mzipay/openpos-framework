package org.jumpmind.pos.trans.model;

import org.jumpmind.pos.persist.AbstractModel;
import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Table;

@Table(name="till_assignment")
public class TillAssignmentModel extends AbstractModel {

    private static final long serialVersionUID = 1L;
    
    @Column(primaryKey=true)
    String tillId;
    
    @Column(primaryKey=true)
    String businessUnitId;
    
    @Column(primaryKey=true) 
    String deviceId;

    public String getTillId() {
        return tillId;
    }

    public void setTillId(String tillId) {
        this.tillId = tillId;
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
       
}
