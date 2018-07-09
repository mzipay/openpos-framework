package org.jumpmind.pos.ops.service;

import org.jumpmind.pos.context.service.ContextService;
import org.jumpmind.pos.ops.model.UnitStatus;
import org.jumpmind.pos.ops.model.UnitStatusRepository;
import org.jumpmind.pos.service.Endpoint;
import org.jumpmind.pos.trans.service.TransService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(transactionManager = "opsTxManager")
public class UpdateUnitStatusEndpoint {

    @Autowired
    UnitStatusRepository unitStatusRepository;
    
    @Autowired
    ContextService contextService;
    
    @Autowired
    TransService transService;
    
    @Endpoint("/changeUnitStatus")
    public StatusChangeResult updateUnitStatus(StatusChangeRequest request) {
        // TODO save transaction first and use its transaction seq 
        long sequenceNumber = contextService.getNextSequence("TRANSACTION");
        UnitStatus status = new UnitStatus();
        status.setBusinessDate(request.getBusinessDay());
        status.setBusinessUnitId(request.getBusinessUnitId());
        status.setSequenceNumber(sequenceNumber);
        status.setCreateTime(request.getTimeOfRequest());
        status.setUnitStatus(request.getNewStatus());
        status.setDeviceId(request.getRequestingDeviceId());
        status.setUnitType(request.getUnitType());
        status.setUnitId(request.getUnitId());
        unitStatusRepository.saveUnitStatus(status);
        StatusChangeResult result = new StatusChangeResult();
        result.setUnitStatus(status);
        return result;
    }
}
