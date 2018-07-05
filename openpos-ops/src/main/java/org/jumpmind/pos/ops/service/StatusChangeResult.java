package org.jumpmind.pos.ops.service;

import org.jumpmind.pos.ops.model.UnitStatus;
import org.jumpmind.pos.service.ServiceResult;

public class StatusChangeResult extends ServiceResult {

    private static final long serialVersionUID = 1L;

    UnitStatus unitStatus;
    
    public void setUnitStatus(UnitStatus status) {
        this.unitStatus = status;
    }
    
    public UnitStatus getUnitStatus() {
        return unitStatus;
    }
}
