package org.jumpmind.pos.trans.service;

import java.util.Date;

import org.jumpmind.pos.service.Endpoint;
import org.jumpmind.pos.service.ServiceResult;
import org.jumpmind.pos.trans.model.TransModel;
import org.jumpmind.pos.trans.model.TransRepository;
import org.jumpmind.pos.trans.model.TransStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(transactionManager = "transTxManager")
public class FinalizeTransEndpoint {

    @Autowired
    TransRepository repository;
    
    @Autowired
    SaveTransQueueEndpoint saveTransactionQueueEndpoint;

    @Endpoint("/transaction/finalize")
    public ServiceResult saveTransaction(SaveTransRequest request) {
        TransModel transaction = request.getTransaction();
        transaction.setEndTime(new Date());
        transaction.setTransStatus(TransStatus.COMPLETED.name());
        repository.save(transaction);
        saveTransactionQueueEndpoint.save(transaction);
        return new ServiceResult();
    }
}
