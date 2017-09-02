package org.jumpmind.jumppos.domain.tax;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

import org.jumpmind.jumppos.domain.BaseEntity;

/**
 * A rule denoting what percentage or dollar amount of tax is applied to a
 * particular taxable total in a RetailTransaction
 * 
 * @author elong
 * 
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_code", discriminatorType = DiscriminatorType.INTEGER)
abstract public class TaxRateRule extends BaseEntity {

    @Id
    private String id;

    @OneToOne
    private TaxGroupRule taxGroupRule;
   
    private Integer taxRateRuleSequenceNumber;

    private BigDecimal minTaxableAmount;

    private BigDecimal maxTaxableAmount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toString() {
        return getClass().getSimpleName() + " [" + taxRateRuleSequenceNumber + "] "
                + minTaxableAmount.setScale(2, BigDecimal.ROUND_HALF_UP) + " to "
                + maxTaxableAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getMaxTaxableAmount() {
        return maxTaxableAmount;
    }

    public void setMaxTaxableAmount(BigDecimal maxTaxableAmount) {
        this.maxTaxableAmount = maxTaxableAmount;
    }

    public BigDecimal getMinTaxableAmount() {
        return minTaxableAmount;
    }

    public void setMinTaxableAmount(BigDecimal minTaxableAmount) {
        this.minTaxableAmount = minTaxableAmount;
    }

    public TaxGroupRule getTaxGroupRule() {
        return taxGroupRule;
    }

    public void setTaxGroupRule(TaxGroupRule taxGroupRule) {
        this.taxGroupRule = taxGroupRule;
    }

    public Integer getTaxRateRuleSequenceNumber() {
        return taxRateRuleSequenceNumber;
    }

    public void setTaxRateRuleSequenceNumber(Integer taxRateRuleSequenceNumber) {
        this.taxRateRuleSequenceNumber = taxRateRuleSequenceNumber;
    }

}
