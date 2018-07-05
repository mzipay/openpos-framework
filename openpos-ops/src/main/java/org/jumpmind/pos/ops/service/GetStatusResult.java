package org.jumpmind.pos.ops.service;

import java.util.List;

import org.jumpmind.pos.ops.model.UnitStatus;
import org.jumpmind.pos.service.ServiceResult;

public class GetStatusResult extends ServiceResult {

    private static final long serialVersionUID = 1L;

    List<UnitStatus> unitStatuses;

    public void setUnitStatuses(List<UnitStatus> unitStatuses) {
        this.unitStatuses = unitStatuses;
    }

    public List<UnitStatus> getUnitStatuses() {
        return unitStatuses;
    }

    public UnitStatus getUnitStatus(String unitId) {
        if (unitStatuses != null) {
            for (UnitStatus unitStatus : unitStatuses) {
                if (unitStatus.getUnitId().equals(unitId)) {
                    return unitStatus;
                }
            }
        }
        return null;
    }
}
