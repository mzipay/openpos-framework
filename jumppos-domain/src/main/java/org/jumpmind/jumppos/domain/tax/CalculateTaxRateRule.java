package org.jumpmind.jumppos.domain.tax;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * A kind of TaxRateRule that denotes a percentage of the taxable total that is
 * to be applied to a RetailTransaction.
 * 
 * @author elong
 * 
 */
@Entity
@DiscriminatorValue("1")
public class CalculateTaxRateRule extends TaxRateRule {

    private BigDecimal percent;

    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }

}
