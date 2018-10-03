package org.jumpmind.pos.item.service;

public class FindItemRequest {

    String itemId;
    
    String businessUnitId;    
    
    public FindItemRequest() {
    }
    
    public FindItemRequest(String itemId) {
        this.itemId = itemId;
    }
    
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    
    public String getItemId() {
        return itemId;
    }

    public void setBusinessUnitId(String businessUnitId) {
        this.businessUnitId = businessUnitId;
    }
    
    public String getBusinessUnitId() {
        return businessUnitId;
    }
    
}
