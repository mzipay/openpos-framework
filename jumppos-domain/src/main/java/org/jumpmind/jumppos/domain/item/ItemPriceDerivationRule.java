package org.jumpmind.jumppos.domain.item;

import java.math.BigDecimal;

import javax.persistence.Entity;


@Entity
public class ItemPriceDerivationRule extends PriceDerivationRule {

    private String reductionMethodCode;
    private BigDecimal reductionPercent;
    
    public ItemPriceDerivationRule() {  
    }
    
    public ItemPriceDerivationRule(String itemPriceDerivationRuleId, String reductionMethodCode, BigDecimal reductionPercent) {
        this.id = itemPriceDerivationRuleId;
        this.reductionMethodCode = reductionMethodCode;
        this.reductionPercent = reductionPercent;
    }

    /**
     * @return Returns the reductionMethodCode.
     */
    public String getReductionMethodCode() {
        return reductionMethodCode;
    }

    /**
     * @param reductionMethodCode
     *            The reductionMethodCode to set.
     */
    public void setReductionMethodCode(String reductionMethodCode) {
        this.reductionMethodCode = reductionMethodCode;
    }

    /**
     * @return Returns the reductionPercent.
     */
    public BigDecimal getReductionPercent() {
        return reductionPercent;
    }

    /**
     * @param reductionPercent
     *            The reductionPercent to set.
     */
    public void setReductionPercent(BigDecimal reductionPercent) {
        this.reductionPercent = reductionPercent;
    }
}
