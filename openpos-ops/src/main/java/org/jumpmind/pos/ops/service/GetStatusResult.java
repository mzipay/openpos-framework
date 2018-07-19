package org.jumpmind.pos.ops.service;

import java.util.List;

import org.jumpmind.pos.ops.model.UnitStatusModel;
import org.jumpmind.pos.service.ServiceResult;

public class GetStatusResult extends ServiceResult {

    private static final long serialVersionUID = 1L;

    List<UnitStatusModel> unitStatuses;

    public void setUnitStatuses(List<UnitStatusModel> unitStatuses) {
        this.unitStatuses = unitStatuses;
    }

    public List<UnitStatusModel> getUnitStatuses() {
        return unitStatuses;
    }

    public UnitStatusModel getUnitStatus(String unitId) {
        if (unitStatuses != null) {
            for (UnitStatusModel unitStatus : unitStatuses) {
                if (unitStatus.getUnitId().equals(unitId)) {
                    return unitStatus;
                }
            }
        }
        return null;
    }
}
