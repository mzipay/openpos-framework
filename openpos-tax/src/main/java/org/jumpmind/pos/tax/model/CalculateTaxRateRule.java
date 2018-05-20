package org.jumpmind.pos.tax.model;

import java.math.BigDecimal;

/**
 * A kind of TaxRateRule that denotes a percentage of the taxable total that is
 * to be applied to a RetailTransaction.
 * 
 * @author elong
 * 
 */
public class CalculateTaxRateRule extends RateRule {

    private BigDecimal percent;

    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }

}
