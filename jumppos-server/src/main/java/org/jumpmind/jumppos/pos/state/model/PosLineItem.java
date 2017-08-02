package org.jumpmind.jumppos.pos.state.model;

public class PosLineItem {

    private String itemId;
    private String description;
    private String extendedAmount;
    private String quantity;
    
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

    public String getExtendedAmount() {
        return extendedAmount;
    }

    public void setExtendedAmount(String extendedAmount) {
        this.extendedAmount = extendedAmount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

}
