package org.jumpmind.jumppos.pos.state.model;


import java.math.BigDecimal;

public class PosLineItem {
    
    private String itemId;
    private String description;
    private BigDecimal extendedAmount;
    
    public String getItemId() {
        return itemId;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public BigDecimal getExtendedAmount() {
        return extendedAmount;
    }
    public void setExtendedAmount(BigDecimal extendedAmount) {
        this.extendedAmount = extendedAmount;
    }
    

}
