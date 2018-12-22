package org.jumpmind.pos.core.screen;


public class ReturnItem extends SellItem {
    private static final long serialVersionUID = 1L;
    
    private String returnQuantity;
    private String extendedPrice;
    
    public String getReturnQuantity() {
        return returnQuantity;
    }

    public void setReturnQuantity(String returnQuantity) {
        this.returnQuantity = returnQuantity;
    }
    
    public void setExtendedPrice(String extendedPrice) {
        this.extendedPrice = extendedPrice;
    }
    
    public String getExtendedPrice() {
        return extendedPrice;
    }
	
}
