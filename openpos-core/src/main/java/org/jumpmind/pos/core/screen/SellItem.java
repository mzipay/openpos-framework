package org.jumpmind.pos.core.screen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jumpmind.pos.core.model.FormDisplayField;

public class SellItem implements IItem, Serializable {
    
    private static final long serialVersionUID = 1L;

    private Integer index;
    private String id;
    private String posItemId;
    private String description;
    private String amount;
    private String originalAmount;
    private String sellingPrice;
    private String quantity;
    private String imageUrl;
    private String productDescription;
    private String discountAmount;
    private List<String> labels = new ArrayList<>();
    private List<FormDisplayField> fields = new ArrayList<>();
    private List<MenuItem> menuItems = new ArrayList<>();
    private boolean selected = false;
    private boolean isGiftReceipt = false;
    
    
    @Override
    public String getID() {
        return this.id;
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }

    @Override
    public Integer getIndex() {
        return this.index;
    }

    @Override
    public void setIndex(Integer index) {
        this.index = index;
    }
    
    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String getAmount() {
        return this.amount;
    }
    
    @Override
    public void setAmount(String amount) {
        this.amount = amount;
    }
    
    @Override
    public String getSubtitle() {
        String subTitle = String.format("%s %s@%s", this.getPosItemId(), this.getQuantity(), this.getSellingPrice());
        return subTitle;
    }
    
    @Override
    public void setSubtitle(String subtitle) {
        // do nothing
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
    
    @Override
    public List<FormDisplayField> getFields() {
        return fields;
    }

    @Override
    public void setFields(List<FormDisplayField> fields) {
        this.fields = fields;
    }
    
    public void addField(FormDisplayField field) {
        this.fields.add(field);
    }

    @Override
    public boolean isSelected() {
        return this.selected;
    }
    
    public void setIsGiftReceipt( boolean giftReceipt ) {
        this.isGiftReceipt = giftReceipt;
    }
    
    public boolean getIsGiftReceipt() {
        return this.isGiftReceipt;
    }
    
    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    public void addMenuItem(MenuItem menuItem) {
        this.menuItems.add(menuItem);
    }
    
    public void setMenuItems(List<MenuItem> transactionMenuItems) {
        this.menuItems = transactionMenuItems;
    }
    
    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

	public String getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(String discountAmount) {
		this.discountAmount = discountAmount;
	}
    
    public String getOriginalAmount() {
    	return this.originalAmount;
    }
    
    public void setOriginalAmount(String originalAmount) {
    	this.originalAmount = originalAmount;
    }

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	} 
	
	public void addLabel( String label ) {
		this.labels.add( label );
	}
}
