package org.jumpmind.pos.trans.model;

import java.sql.Types;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Table;

@Table(name="transaction_queue")
public class TransactionQueueModel  extends TransactionEntity {
    
    @Column(type=Types.CLOB)
    String payload;
    
    public TransactionQueueModel() {
    }
    
    public TransactionQueueModel(TransactionModel transaction, String payload) {
        this.setBusinessDate(transaction.getBusinessDate());
        this.setSequenceNumber(transaction.getSequenceNumber());
        this.setDeviceId(transaction.getDeviceId());
        this.payload = payload;
    }
    
    public void setPayload(String payload) {
        this.payload = payload;
    }
    
    public String getPayload() {
        return payload;
    }
}
