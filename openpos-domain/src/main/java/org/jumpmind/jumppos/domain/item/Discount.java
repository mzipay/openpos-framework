package org.jumpmind.jumppos.domain.item;

import java.math.BigDecimal;

import org.jumpmind.jumppos.domain.BaseEntity;

public class Discount extends BaseEntity {

    private String discountMethodCode;
    private String description;
    private BigDecimal amount;
    private BigDecimal percent;
    private BigDecimal newPrice;
    private String applyToCode;

    public String getDiscountMethodCode() {
        return discountMethodCode;
    }

    public void setDiscountMethodCode(String discountMethod) {
        this.discountMethodCode = discountMethod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }

    public BigDecimal getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(BigDecimal newPrice) {
        this.newPrice = newPrice;
    }

    public String getApplyToCode() {
        return applyToCode;
    }

    public void setApplyToCode(String applyToCode) {
        this.applyToCode = applyToCode;
    }
}
