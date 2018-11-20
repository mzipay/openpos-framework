package org.jumpmind.pos.core.screen;


import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public class SellItem extends DefaultItem {
    
    private static final long serialVersionUID = 1L;
    
    private String posItemId;
    private String altItemId;
    private String originalAmount;
    private String sellingPrice;
    private String quantity;
    private String discountAmount;
    private String salesAssociate;
    private List<MenuItem> menuItems = new ArrayList<>();
    private boolean isGiftReceipt = false;
    private boolean isQuantityChangeable;
    
    /**
     * Put properties in this map if they are optional. When not set, they don't
     * show up in the json which means less overhead.
     */
    private Map<String, Object> optionalProperties = new HashMap<String, Object>();

    @Override
    public String getSubtitle() {
        String subTitle = "Item: %s%s %s@%s";
        String altItemId = isBlank(this.getAltItemId()) ? "" : "/" + this.getAltItemId();
        if (this.salesAssociate != null && this.salesAssociate != "") {
            subTitle = subTitle + " - Sales Associate: %s";
            subTitle = String.format(subTitle, this.getPosItemId(), altItemId, this.getQuantity(), this.getSellingPrice(), this.getSalesAssociate());
        } else {
            subTitle = String.format(subTitle, this.getPosItemId(), altItemId, this.getQuantity(), this.getSellingPrice());
        }

        return subTitle;
    }
    
    @Override
    public void setSubtitle(String subtitle) {
        // do nothing
    	// Oh.
    }

    public void setPosItemId(String posItemId) {
        this.posItemId = posItemId;
    }
    
    public String getPosItemId() {
        return posItemId;
    }
    
    public void setAltItemId(String altItemId) {
        this.altItemId = altItemId;
    }
    
    public String getAltItemId() {
        return altItemId;
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
    public void setIsGiftReceipt( boolean giftReceipt ) {
        this.isGiftReceipt = giftReceipt;
    }
    
    public boolean getIsGiftReceipt() {
        return this.isGiftReceipt;
    }
    public void addMenuItem(MenuItem menuItem) {
        this.menuItems.add(menuItem);
    }
    
    public void setMenuItems(List<MenuItem> transactionMenuItems) {
        this.menuItems = transactionMenuItems;
    }
    
    public void addMenuItems(MenuItem... transactionMenuItems) {
        if (this.menuItems == null) {
            this.setMenuItems(Arrays.asList(transactionMenuItems));
        } else {
            this.menuItems.addAll(Arrays.asList(transactionMenuItems));
        }
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
	
	public String getSalesAssociate() {
		return salesAssociate;
	}

	public void setSalesAssociate(String salesAssociate) {
		this.salesAssociate = salesAssociate;
	}
    
    public String getOriginalAmount() {
    	return this.originalAmount;
    }
    
    public void setOriginalAmount(String originalAmount) {
    	this.originalAmount = originalAmount;
    }
	
    @JsonAnyGetter
    public Map<String, Object> any() {
        return this.optionalProperties;
    }

    @JsonAnySetter
    public void put(String name, Object value) {
        this.optionalProperties.put(name, value);
    }

    public boolean contains(String name) {
        return this.optionalProperties.containsKey(name);
    }

    public Object get(String name) {
        return optionalProperties.get(name);
    }

    public boolean isQuantityChangeable() {
        return isQuantityChangeable;
    }

    public void setQuantityChangeable(boolean isQuantityChangeable) {
        this.isQuantityChangeable = isQuantityChangeable;
    }
}
