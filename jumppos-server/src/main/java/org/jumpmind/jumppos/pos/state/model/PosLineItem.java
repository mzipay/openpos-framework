package org.jumpmind.jumppos.pos.state.model;

public class PosLineItem {

    private int lineNumber;
    private String itemId;
    private String posItemId;
    private String description;
    private String extendedAmount;

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
    
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
    
    public int getLineNumber() {
        return lineNumber;
    }
    
    public void setPosItemId(String posItemId) {
        this.posItemId = posItemId;
    }
    
    public String getPosItemId() {
        return posItemId;
    }

}
