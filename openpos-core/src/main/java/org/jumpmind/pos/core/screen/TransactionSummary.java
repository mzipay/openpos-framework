package org.jumpmind.pos.core.screen;

import java.io.Serializable;

public class TransactionSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    String transactionId;

    String description;

    String grandTotal;

    String workstationId;

    public TransactionSummary() {
    }

    public TransactionSummary(String transactionId, String description, String grandTotal, String workstationId) {
        this.transactionId = transactionId;
        this.description = description;
        this.grandTotal = grandTotal;
        this.workstationId = workstationId;
    }

    public void setWorkstationId(String workstationId) {
        this.workstationId = workstationId;
    }

    public String getWorkstationId() {
        return workstationId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

}
