package org.jumpmind.pos.trans.service;

import org.jumpmind.pos.service.ServiceResult;
import org.jumpmind.pos.trans.model.TransactionModel;

public class CreateTransResult extends ServiceResult {

    private static final long serialVersionUID = 1L;
    
    TransactionModel transaction;
    
    public CreateTransResult(TransactionModel transaction) {
        this.transaction = transaction;
    }
    
    public CreateTransResult() {
    }
    
    public TransactionModel getTransaction() {
        return transaction;
    }

}
