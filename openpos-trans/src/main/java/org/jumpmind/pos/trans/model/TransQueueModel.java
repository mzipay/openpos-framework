package org.jumpmind.pos.trans.model;

import java.sql.Types;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Table;

@Table(name="queue")
public class TransQueueModel  extends AbstractTransModel {
    
    private static final long serialVersionUID = 1L;

    @Column(type=Types.CLOB)
    String payload;
    
    @Column
    String transStatus;
    
    public TransQueueModel() {
    }
    
    public TransQueueModel(TransModel transaction, String payload) {
        this.setBusinessDate(transaction.getBusinessDate());
        this.setSequenceNumber(transaction.getSequenceNumber());
        this.setDeviceId(transaction.getDeviceId());
        this.transStatus = transaction.getTransStatus();
        this.payload = payload;
    }
    
    public void setPayload(String payload) {
        this.payload = payload;
    }
    
    public String getPayload() {
        return payload;
    }
    
    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }
    
    public String getTransStatus() {
        return transStatus;
    }
}
