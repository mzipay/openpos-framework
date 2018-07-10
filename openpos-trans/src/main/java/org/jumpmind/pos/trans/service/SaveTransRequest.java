package org.jumpmind.pos.trans.service;

import java.io.Serializable;

import org.jumpmind.pos.trans.model.Transaction;

public class SaveTransRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    
    Transaction transaction;
    
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
    
    public Transaction getTransaction() {
        return transaction;
    }
}
