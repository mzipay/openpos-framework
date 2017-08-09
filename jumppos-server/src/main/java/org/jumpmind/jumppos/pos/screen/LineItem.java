package org.jumpmind.jumppos.pos.screen;

import java.io.Serializable;

public class LineItem implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private int lineNumber;
    private String itemId;
    private String posItemId;
    private String description;
    private String extendedAmount;
    private String sellingPrice;
    private String quantity;
    private String imageUrl;
    private String productDescription;
    
    
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
    
    public String getSellingPrice() {
        return sellingPrice;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public String getProductDescription() {
        return productDescription;
    }
    
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
}
