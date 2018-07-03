package org.jumpmind.pos.ops.model;

import java.io.Serializable;

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
      

}
