package org.jumpmind.pos.trans.service;

import java.io.Serializable;

import org.jumpmind.pos.trans.model.TransactionModel;

public class SaveTransRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    
    TransactionModel transaction;
    
    public SaveTransRequest(TransactionModel model) {
        this.transaction = model;
    }
    
    public SaveTransRequest() {
    }
    
    public void setTransaction(TransactionModel transaction) {
        this.transaction = transaction;
    }
    
    public TransactionModel getTransaction() {
        return transaction;
    }
}
