package org.jumpmind.pos.context.model;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.AbstractModel;
import org.jumpmind.pos.persist.Table;

@Table(name="business_unit")
public class BusinessUnitModel extends AbstractModel {

    private static final long serialVersionUID = 1L;
    @Column(primaryKey = true)
    private String businessUnitId;
    
    public void setBusinessUnitId(String id) {
        this.businessUnitId = id;
    }
    
    public String getBusinessUnitId() {
        return businessUnitId;
    }
}
