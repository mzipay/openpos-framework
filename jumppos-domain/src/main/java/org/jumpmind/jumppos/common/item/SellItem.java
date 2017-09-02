package org.jumpmind.jumppos.common.item;

import java.math.BigDecimal;

import org.jumpmind.jumppos.domain.business.Customer;
import org.jumpmind.jumppos.domain.item.BusinessUnitGroupItem;
import org.jumpmind.jumppos.domain.transaction.EntryMethod;

public class SellItem {

    private String posItemId;

    private BusinessUnitGroupItem item;

    private BigDecimal priceOverride;
    
    private EntryMethod entryMethod;
    
    private Customer customer;
    
    private BigDecimal quantity;

    public SellItem(String posItemId) {
        this.posItemId = posItemId;
    }
    
    public void setItem(BusinessUnitGroupItem item) {
        this.item = item;
    }
    
    public BusinessUnitGroupItem getItem() {
        return item;
    }
    
    public void setPriceOverride(BigDecimal priceOverride) {
        this.priceOverride = priceOverride;
    }
    
    public BigDecimal getPriceOverride() {
        return priceOverride;
    }
    
    public String getPosItemId() {
        return posItemId;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setEntryMethod(EntryMethod entryMethod) {
        this.entryMethod = entryMethod;
    }
    
    public EntryMethod getEntryMethod() {
        return entryMethod;
    }
    
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getQuantity() {
        return quantity;
    }
    
}
