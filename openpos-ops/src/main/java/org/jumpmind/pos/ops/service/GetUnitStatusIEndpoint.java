package org.jumpmind.pos.ops.service;

import org.jumpmind.pos.ops.model.UnitStatusRepository;
import org.jumpmind.pos.service.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(transactionManager = "opsTxManager")
public class GetUnitStatusIEndpoint {

    @Autowired
    UnitStatusRepository unitStatusRepository;
    
    @Endpoint("/unitStatus/{unitType}/{unitId}")
    public GetStatusResult getUnitStatus(String unitType, String unitId) {        
        GetStatusResult result = new GetStatusResult();
        result.setUnitStatuses(unitStatusRepository.findUnitStatus(unitType, unitId));
        return result;
    }
}
