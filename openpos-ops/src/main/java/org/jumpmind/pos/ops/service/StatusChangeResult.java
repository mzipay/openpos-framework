package org.jumpmind.pos.ops.service;

import org.jumpmind.pos.ops.model.UnitStatusModel;
import org.jumpmind.pos.service.ServiceResult;

public class StatusChangeResult extends ServiceResult {

    private static final long serialVersionUID = 1L;

    UnitStatusModel unitStatus;
    
    public void setUnitStatus(UnitStatusModel status) {
        this.unitStatus = status;
    }
    
    public UnitStatusModel getUnitStatus() {
        return unitStatus;
    }
}
