package org.jumpmind.jumppos.domain.tax;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * A kind of TaxRateRule that denotes a monetary amount of tax that is to be
 * applied to a RetailTransaction.
 * 
 * @author elong
 * 
 */
@Entity
@DiscriminatorValue("2")
public class FlatTaxRateRule extends TaxRateRule {

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
