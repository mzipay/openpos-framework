package org.jumpmind.pos.trans.service;

import java.util.Date;

import org.jumpmind.pos.context.service.ContextService;
import org.jumpmind.pos.service.Endpoint;
import org.jumpmind.pos.trans.model.TransactionModel;
import org.jumpmind.pos.trans.model.TransactionRepository;
import org.jumpmind.pos.trans.model.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(transactionManager = "transTxManager")
public class CreateTransEndpoint {

    @Autowired
    TransactionRepository repository;
    
    @Autowired
    ContextService contextService;
    
    @Autowired
    SaveTransactionQueueEndpoint saveTransactionQueueEndpoint;
    
    @Endpoint("/transaction/create")
    public CreateTransResult createTransaction(CreateTransRequest request) {
        long sequenceNumber = contextService.getNextSequence("TRANSACTION");
        TransactionModel transaction = new TransactionModel();
        transaction.setSequenceNumber(sequenceNumber);
        transaction.setBusinessDate(request.getBusinessDate());
        transaction.setBusinessUnitId(request.getBusinessUnitId());
        transaction.setDeviceId(request.getDeviceId());
        transaction.setTransType(request.getTransType());
        transaction.setTransStatus(TransactionStatus.IN_PROGRESS.name());
        transaction.setBeginTime(new Date());
        saveTransactionQueueEndpoint.save(transaction);
        return new CreateTransResult(transaction);
    }
}
