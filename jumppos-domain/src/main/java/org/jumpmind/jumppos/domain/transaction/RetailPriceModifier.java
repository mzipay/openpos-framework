package org.jumpmind.jumppos.domain.transaction;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.jumpmind.jumppos.domain.BaseEntity;

@Entity
public class RetailPriceModifier extends BaseEntity {

    @Id
    private String id;
    
    @OneToOne
    SaleReturnLineItem lineItem;
    
    private RetailPriceModifierReason reasonCode;
    private BigDecimal percent;
    private BigDecimal amount;
    private BigDecimal previousPrice;
    private BigDecimal newPrice;

    public RetailPriceModifier() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RetailPriceModifierReason getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(RetailPriceModifierReason reasonCode) {
        this.reasonCode = reasonCode;
    }

    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPreviousPrice() {
        return previousPrice;
    }

    public void setPreviousPrice(BigDecimal previousPrice) {
        this.previousPrice = previousPrice;
    }

    public BigDecimal getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(BigDecimal newPrice) {
        this.newPrice = newPrice;
    }

    public void setLineItem(SaleReturnLineItem lineItem) {
        this.lineItem = lineItem;
    }
    
    public SaleReturnLineItem getLineItem() {
        return lineItem;
    }
}
