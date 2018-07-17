package org.jumpmind.pos.trans.service;

import java.util.Date;

import org.jumpmind.pos.context.service.ContextService;
import org.jumpmind.pos.service.Endpoint;
import org.jumpmind.pos.trans.model.TransModel;
import org.jumpmind.pos.trans.model.TransRepository;
import org.jumpmind.pos.trans.model.TransStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(transactionManager = "transTxManager")
public class CreateTransEndpoint {

    @Autowired
    TransRepository repository;
    
    @Autowired
    ContextService contextService;
    
    @Autowired
    SaveTransQueueHelper saveTransactionQueueEndpoint;
    
    @Endpoint("/transaction/create")
    public CreateTransResult createTransaction(CreateTransRequest request) {
        long sequenceNumber = contextService.getNextSequence("TRANSACTION");
        TransModel transaction = new TransModel();
        transaction.setSequenceNumber(sequenceNumber);
        transaction.setBusinessDate(request.getBusinessDate());
        transaction.setBusinessUnitId(request.getBusinessUnitId());
        transaction.setDeviceId(request.getDeviceId());
        transaction.setTransType(request.getTransType());
        transaction.setTransStatus(TransStatus.IN_PROGRESS.name());
        transaction.setBeginTime(new Date());
        transaction.setOperatorUsername(request.getUsername());
        saveTransactionQueueEndpoint.aSyncSave(transaction);
        return new CreateTransResult(transaction);
    }
}
