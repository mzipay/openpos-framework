package org.jumpmind.pos.tax.model;

import java.math.BigDecimal;

/**
 * A kind of TaxRateRule that denotes a monetary amount of tax that is to be
 * applied to a RetailTransaction.
 * 
 * @author elong
 * 
 */
public class FlatTaxRateRule extends RateRule {

    private BigDecimal amount;

    public String toString() {
        return super.toString() + " -> " + amount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
