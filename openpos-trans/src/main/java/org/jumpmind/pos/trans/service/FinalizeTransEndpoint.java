package org.jumpmind.pos.trans.service;

import java.util.Date;

import org.jumpmind.pos.service.Endpoint;
import org.jumpmind.pos.service.ServiceResult;
import org.jumpmind.pos.trans.model.TransactionModel;
import org.jumpmind.pos.trans.model.TransactionRepository;
import org.jumpmind.pos.trans.model.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(transactionManager = "transTxManager")
public class FinalizeTransEndpoint {

    @Autowired
    TransactionRepository repository;

    @Endpoint("/transaction/finalize")
    public ServiceResult saveTransaction(SaveTransRequest request) {
        TransactionModel transaction = request.getTransaction();
        transaction.setEndTime(new Date());
        transaction.setTransStatus(TransactionStatus.COMPLETED.name());
        repository.save(transaction);
        return new ServiceResult();
    }
}
