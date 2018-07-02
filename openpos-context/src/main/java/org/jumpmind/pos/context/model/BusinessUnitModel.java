package org.jumpmind.pos.context.model;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Entity;
import org.jumpmind.pos.persist.Table;

@Table(name="business_unit")
public class BusinessUnitModel extends Entity {

    @Column(primaryKey = true)
    private String businessUnitId;
    
    public void setBusinessUnitId(String id) {
        this.businessUnitId = id;
    }
    
    public String getBusinessUnitId() {
        return businessUnitId;
    }
}
