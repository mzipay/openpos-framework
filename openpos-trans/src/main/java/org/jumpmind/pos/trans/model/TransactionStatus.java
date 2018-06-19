package org.jumpmind.pos.trans.model;

public final class TransactionStatus {

    private TransactionStatus() {
    }
    
    public final int IN_PROGRESS = 1;
    public final int COMPLETED = 2;
    public final int CANCELLED = 3;
    public final int SUSPENDED = 4;
    public final int FAILED = 5;
    public final int SUSPEND_RETREIVED = 6;
    public final int SUSPEND_CANCELLED = 7;
    public final int VOIDED = 8;
}
