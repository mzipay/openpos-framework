package org.jumpmind.jumppos.pos.state.model;


import java.math.BigDecimal;

public class TenderLineItem {
    
    private String tenderType;
    private BigDecimal amount;
    
    public String getTenderType() {
        return tenderType;
    }
    public void setTenderType(String tenderType) {
        this.tenderType = tenderType;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
