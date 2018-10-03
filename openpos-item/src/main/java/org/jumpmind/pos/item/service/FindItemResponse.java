package org.jumpmind.pos.item.service;

import org.jumpmind.pos.item.model.BusinessUnitItemModel;

public class FindItemResponse {

    BusinessUnitItemModel item;
    
    public FindItemResponse(BusinessUnitItemModel item) {
        this.item = item;
    }
    
    public FindItemResponse() {
    }
    
    public BusinessUnitItemModel getItem() {
        return item;
    }
    
    public void setItem(BusinessUnitItemModel item) {
        this.item = item;
    }
    
}
