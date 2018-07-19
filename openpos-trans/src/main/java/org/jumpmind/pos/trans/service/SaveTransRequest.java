package org.jumpmind.pos.trans.service;

import java.io.Serializable;

import org.jumpmind.pos.trans.model.TransModel;

public class SaveTransRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    
    TransModel transaction;
    
    public SaveTransRequest(TransModel model) {
        this.transaction = model;
    }
    
    public SaveTransRequest() {
    }
    
    public void setTransaction(TransModel transaction) {
        this.transaction = transaction;
    }
    
    public TransModel getTransaction() {
        return transaction;
    }
}
