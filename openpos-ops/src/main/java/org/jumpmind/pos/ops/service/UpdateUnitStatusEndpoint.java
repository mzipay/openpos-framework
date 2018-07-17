package org.jumpmind.pos.ops.service;

import org.jumpmind.pos.context.service.ContextService;
import org.jumpmind.pos.ops.model.UnitStatusModel;
import org.jumpmind.pos.ops.model.UnitStatusRepository;
import org.jumpmind.pos.service.Endpoint;
import org.jumpmind.pos.trans.model.TransModel;
import org.jumpmind.pos.trans.service.CreateTransRequest;
import org.jumpmind.pos.trans.service.SaveTransRequest;
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

    @Endpoint("/updateUnitStatus")
    public StatusChangeResult updateUnitStatus(StatusChangeRequest request) {
        UnitStatusModel status = new UnitStatusModel();
        status.setBusinessDate(request.getBusinessDate());
        status.setBusinessUnitId(request.getBusinessUnitId());
        status.setCreateTime(request.getTimeOfRequest());
        status.setUnitStatus(request.getNewStatus());
        status.setDeviceId(request.getRequestingDeviceId());
        status.setUnitType(request.getUnitType());
        status.setUnitId(request.getUnitId());

        TransModel transaction = transService.createTransaction(new CreateTransRequest(request.getBusinessDate(),
                request.getRequestingDeviceId(), request.getBusinessUnitId(), status.toTransactionType().name(), request.getUsername())).getTransaction();
        
        status.setSequenceNumber(transaction.getSequenceNumber());

        unitStatusRepository.saveUnitStatus(status);
        transService.finalizeTransaction(new SaveTransRequest(transaction));
        
        StatusChangeResult result = new StatusChangeResult();
        result.setUnitStatus(status);
        return result;
    }
}
