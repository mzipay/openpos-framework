package org.jumpmind.pos.item.model;

import java.math.BigDecimal;
import java.util.Date;

import org.jumpmind.pos.persist.AbstractModel;
import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Table;

@Table(name="selling_price")
public class SellingPriceModel extends AbstractModel {

    private static final long serialVersionUID = 1L;

    @Column(primaryKey=true)
    String sellingPriceId;
    
    @Column
    Date effectiveStartTime;
    
    @Column 
    Date effectiveEndTime;
    
    @Column
    BigDecimal permanentPrice;
    
    @Column
    BigDecimal currentSalePrice;

    public String getSellingPriceId() {
        return sellingPriceId;
    }

    public void setSellingPriceId(String sellingPriceId) {
        this.sellingPriceId = sellingPriceId;
    }

    public Date getEffectiveStartTime() {
        return effectiveStartTime;
    }

    public void setEffectiveStartTime(Date effectiveStartTime) {
        this.effectiveStartTime = effectiveStartTime;
    }

    public Date getEffectiveEndTime() {
        return effectiveEndTime;
    }

    public void setEffectiveEndTime(Date effectiveEndTime) {
        this.effectiveEndTime = effectiveEndTime;
    }

    public BigDecimal getPermanentPrice() {
        return permanentPrice;
    }

    public void setPermanentPrice(BigDecimal permanentPrice) {
        this.permanentPrice = permanentPrice;
    }

    public BigDecimal getCurrentSalePrice() {
        return currentSalePrice;
    }

    public void setCurrentSalePrice(BigDecimal currentSalePrice) {
        this.currentSalePrice = currentSalePrice;
    }        
        
}
