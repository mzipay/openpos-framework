package org.jumpmind.pos.trans.model;

public final class TransactionTypeConstants {
    
    private TransactionTypeConstants() {
    }
    
    public final int SALE = 1;
    public final int RETURN = 2;
    public final int VOID = 3;
    public final int NO_SALE = 4;
    public final int OPEN_BUSINESS_UNIT = 6;
    public final int CLOSE_BUSINESS_UNIT = 7;
    public final int OPEN_DEVICE = 8;
    public final int CLOSE_DEVICE = 9;

}
