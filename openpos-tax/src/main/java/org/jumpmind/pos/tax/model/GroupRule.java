package org.jumpmind.pos.tax.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Entity;
import org.jumpmind.pos.persist.Table;

/**
 * A rule that prescribes how a particular tax is to be applied to a group
 * Items.
 * 
 * @author elong
 * 
 */
@Table(description = "A rule that prescribes how a particular tax is to be applied to a group Items.")
public class GroupRule extends Entity implements Comparable<GroupRule> {

    @Column(primaryKey = true)
    private String id;
    
    @Column(primaryKey = true)
    private String authorityId;
    
    @Column(primaryKey = true)
    private String groupdId;
    
    // TODO tax type?
    // TODO tax holiday flag?
    // TODO effective time
    // TODO customer group?

    @Column()
    private String ruleName;

    @Column()
    private String description;

    @Column()
    private Integer compoundSequenceNumber;

    @Column()
    private Boolean taxOnGrossAmountFlag;

    @Column()
    private String calculationMethodCode;

    @Column()
    private String taxRateRuleUsageCode;

    @Column()
    private BigDecimal cycleAmount;

    private Group taxableGroup;

    private Collection<RateRule> taxRateRules;
    
    private Authority authority;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getAuthorityId() {
        return authorityId;
    }
    
    public void setAuthorityId(String authorityId) {
        this.authorityId = authorityId;
    }

    public String toString() {
        return "TaxGroupRule " + authorityId + "-" + taxableGroup.getId() + "-" + compoundSequenceNumber;
    }

    public boolean equals(Object o) {
        if (o != null && o instanceof GroupRule) {
            GroupRule taxGroupRule = (GroupRule) o;
            return taxGroupRule.getAuthorityId().equals(authorityId) && taxGroupRule.getTaxableGroup().equals(taxableGroup);
        }
        return false;
    }

    public int compareTo(GroupRule o) {
        int compare = -1;
        if (o != null && o instanceof GroupRule) {
            GroupRule taxGroupRule = (GroupRule) o;
            compare = taxGroupRule.getAuthorityId().compareTo(authorityId);
            if (compare == 0) {
                compare = taxGroupRule.getTaxableGroup().compareTo(taxableGroup);
            }
        }
        return compare;
    }

    public void addTaxRateRule(RateRule taxRateRule) {
        if (taxRateRules == null) {
            taxRateRules = new ArrayList<RateRule>();
        }
        taxRateRules.add(taxRateRule);
    }

    public RateRule getFirstTaxRateRule() {
        if (taxRateRules != null && taxRateRules.size() > 0) {
            return taxRateRules.iterator().next();
        }
        return null;
    }

    public RateRule getLastTaxRateRule() {
        RateRule rateRule = null;
        if (taxRateRules != null) {
            Iterator<RateRule> iter = taxRateRules.iterator();
            while (iter.hasNext()) {
                rateRule = iter.next();
            }
        }
        return rateRule;
    }

    public BigDecimal getTaxPercent() {
        if (taxRateRules != null && taxRateRules.size() == 1) {
            RateRule rateRule = taxRateRules.iterator().next();
            if (rateRule instanceof CalculateTaxRateRule) {
                CalculateTaxRateRule calcRateRule = (CalculateTaxRateRule) rateRule;
                return calcRateRule.getPercent();
            }
        }
        return null;
    }

    public boolean usesCompounding() {
        return compoundSequenceNumber != null && compoundSequenceNumber.intValue() > 0;
    }

    public String getCalculationMethodCode() {
        return calculationMethodCode;
    }

    public void setCalculationMethodCode(String calculationMethodCode) {
        this.calculationMethodCode = calculationMethodCode;
    }

    public Integer getCompoundSequenceNumber() {
        return compoundSequenceNumber;
    }

    public void setCompoundSequenceNumber(Integer compoundSequenceNumber) {
        this.compoundSequenceNumber = compoundSequenceNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String name) {
        this.ruleName = name;
    }

    public Group getTaxableGroup() {
        return taxableGroup;
    }

    public void setTaxableGroup(Group taxableGroup) {
        this.taxableGroup = taxableGroup;
    }

    public Boolean getTaxOnGrossAmountFlag() {
        return taxOnGrossAmountFlag;
    }

    public void setTaxOnGrossAmountFlag(Boolean taxOnGrossAmountFlag) {
        this.taxOnGrossAmountFlag = taxOnGrossAmountFlag;
    }

    public Collection<RateRule> getTaxRateRules() {
        return taxRateRules;
    }

    public void setTaxRateRules(Collection<RateRule> taxRateRules) {
        this.taxRateRules = taxRateRules;
    }

    public String getTaxRateRuleUsageCode() {
        return taxRateRuleUsageCode;
    }

    public void setTaxRateRuleUsageCode(String taxRateRuleUsageCode) {
        this.taxRateRuleUsageCode = taxRateRuleUsageCode;
    }

    public BigDecimal getCycleAmount() {
        return cycleAmount;
    }

    public void setCycleAmount(BigDecimal centsPerCycle) {
        this.cycleAmount = centsPerCycle;
    }

    public String getGroupdId() {
        return groupdId;
    }

    public void setGroupdId(String groupdId) {
        this.groupdId = groupdId;
    }
    
    public void setAuthority(Authority authority) {
        this.authority = authority;
    }
    
    public Authority getAuthority() {
        return authority;
    }

}
