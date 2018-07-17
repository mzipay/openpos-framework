package org.jumpmind.pos.trans.service;

import org.jumpmind.pos.service.ServiceResult;
import org.jumpmind.pos.trans.model.TransModel;

public class CreateTransResult extends ServiceResult {

    private static final long serialVersionUID = 1L;
    
    TransModel transaction;
    
    public CreateTransResult(TransModel transaction) {
        this.transaction = transaction;
    }
    
    public CreateTransResult() {
    }
    
    public TransModel getTransaction() {
        return transaction;
    }

}
