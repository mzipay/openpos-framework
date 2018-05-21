package org.jumpmind.pos.tax.model;

import java.math.BigDecimal;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Entity;
import org.jumpmind.pos.persist.Table;

/**
 * A rule denoting what percentage or dollar amount of tax is applied to a
 * particular taxable total in a RetailTransaction.
 * 
 * @author elong
 * 
 */
@Table(
        description = "A rule denoting what percentage or dollar amount of tax is applied to a particular taxable total in a RetailTransaction.")
abstract public class RateRule extends Entity {

    @Column(primaryKey = true)
    private String id;

    @Column(primaryKey = true)
    private String authorityId;

    @Column(primaryKey = true)
    private String groupId;

    @Column(primaryKey = true)
    private Integer rateRuleSequenceNumber;

    // TODO tax type
    // TODO tax holiday

    @Column()
    private BigDecimal minTaxableAmount;

    @Column()
    private BigDecimal maxTaxableAmount;

    private GroupRule groupRule;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toString() {
        return getClass().getSimpleName() + " [" + rateRuleSequenceNumber + "] " + minTaxableAmount.setScale(2, BigDecimal.ROUND_HALF_UP)
                + " to " + maxTaxableAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
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

    public GroupRule getGroupRule() {
        return groupRule;
    }

    public void setGroupRule(GroupRule groupRule) {
        this.groupRule = groupRule;
    }

    public Integer getRateRuleSequenceNumber() {
        return rateRuleSequenceNumber;
    }

    public void setRateRuleSequenceNumber(Integer rateRuleSequenceNumber) {
        this.rateRuleSequenceNumber = rateRuleSequenceNumber;
    }

}
