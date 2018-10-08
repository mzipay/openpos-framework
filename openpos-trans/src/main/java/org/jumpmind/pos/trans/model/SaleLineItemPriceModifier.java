package org.jumpmind.pos.trans.model;

import java.math.BigDecimal;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Table;

@Table(name = "sale_line_item_price_mod")
public class SaleLineItemPriceModifier extends AbstractTransLineModel {

    private static final long serialVersionUID = 1L;

    @Column(primaryKey = true)
    int modLineSequenceNumber;

    @Column
    String username;
    
    @Column
    String reasonCode;
    
    @Column
    BigDecimal modByPercentage;
    
    @Column
    BigDecimal modByAmount;
    
    @Column
    BigDecimal oldPriceAmount;
    
    @Column
    BigDecimal newPriceAmount;
    
    @Column
    PriceModCalcMethodCode calcMethod;
    
    @Column
    String promotionId;

    public int getModLineSequenceNumber() {
        return modLineSequenceNumber;
    }

    public void setModLineSequenceNumber(int modLineSequenceNumber) {
        this.modLineSequenceNumber = modLineSequenceNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public BigDecimal getModByPercentage() {
        return modByPercentage;
    }

    public void setModByPercentage(BigDecimal modByPercentage) {
        this.modByPercentage = modByPercentage;
    }

    public BigDecimal getModByAmount() {
        return modByAmount;
    }

    public void setModByAmount(BigDecimal modByAmont) {
        this.modByAmount = modByAmont;
    }

    public BigDecimal getOldPriceAmount() {
        return oldPriceAmount;
    }

    public void setOldPriceAmount(BigDecimal oldPriceAmount) {
        this.oldPriceAmount = oldPriceAmount;
    }

    public BigDecimal getNewPriceAmount() {
        return newPriceAmount;
    }

    public void setNewPriceAmount(BigDecimal newPriceAmount) {
        this.newPriceAmount = newPriceAmount;
    }

    public PriceModCalcMethodCode getCalcMethod() {
        return calcMethod;
    }

    public void setCalcMethod(PriceModCalcMethodCode calcMethod) {
        this.calcMethod = calcMethod;
    }
    
    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }
    
    public String getPromotionId() {
        return promotionId;
    }
    
    
    
}
