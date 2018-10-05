package org.jumpmind.pos.trans.model;

import java.math.BigDecimal;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Table;

@Table(name = "tax_sale_line_item")
public class TaxSaleLineItemModel extends AbstractTransLineModel {

    private static final long serialVersionUID = 1L;

    @Column(primaryKey = true)
    int taxLineSequenceNumber;
    
    @Column
    String authorityId;
    
    @Column
    String jursidictionId;
    
    @Column
    String groupRuleId;
    
    @Column
    String groupId;
    
    @Column
    String rateRuleId;
    
    @Column
    int rateRuleSequenceNumber;
        
    @Column
    boolean overrideApplied;
    
    @Column
    String taxExemptId;
    
    @Column
    boolean taxExempt;
    
    @Column
    BigDecimal taxExemptAmount;
    
    @Column
    BigDecimal overridePercent;
    
    @Column
    BigDecimal overrideAmount;
    
    @Column
    String overrideReasonCode;

    @Column
    BigDecimal taxPercentage;

    @Column
    BigDecimal taxAmount;
    
    @Column
    BigDecimal taxableAmount;

    public int getTaxLineSequenceNumber() {
        return taxLineSequenceNumber;
    }

    public void setTaxLineSequenceNumber(int taxLineSequenceNumber) {
        this.taxLineSequenceNumber = taxLineSequenceNumber;
    }

    public String getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(String authorityId) {
        this.authorityId = authorityId;
    }

    public String getJursidictionId() {
        return jursidictionId;
    }

    public void setJursidictionId(String jursidictionId) {
        this.jursidictionId = jursidictionId;
    }

    public String getGroupRuleId() {
        return groupRuleId;
    }

    public void setGroupRuleId(String groupRuleId) {
        this.groupRuleId = groupRuleId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getRateRuleId() {
        return rateRuleId;
    }

    public void setRateRuleId(String rateRuleId) {
        this.rateRuleId = rateRuleId;
    }

    public int getRateRuleSequenceNumber() {
        return rateRuleSequenceNumber;
    }

    public void setRateRuleSequenceNumber(int rateRuleSequenceNumber) {
        this.rateRuleSequenceNumber = rateRuleSequenceNumber;
    }

    public boolean isOverrideApplied() {
        return overrideApplied;
    }

    public void setOverrideApplied(boolean overrideApplied) {
        this.overrideApplied = overrideApplied;
    }

    public String getTaxExemptId() {
        return taxExemptId;
    }

    public void setTaxExemptId(String taxExemptId) {
        this.taxExemptId = taxExemptId;
    }

    public boolean isTaxExempt() {
        return taxExempt;
    }

    public void setTaxExempt(boolean taxExempt) {
        this.taxExempt = taxExempt;
    }

    public BigDecimal getTaxExemptAmount() {
        return taxExemptAmount;
    }

    public void setTaxExemptAmount(BigDecimal taxExemptAmount) {
        this.taxExemptAmount = taxExemptAmount;
    }

    public BigDecimal getOverridePercent() {
        return overridePercent;
    }

    public void setOverridePercent(BigDecimal overridePercent) {
        this.overridePercent = overridePercent;
    }

    public BigDecimal getOverrideAmount() {
        return overrideAmount;
    }

    public void setOverrideAmount(BigDecimal overrideAmount) {
        this.overrideAmount = overrideAmount;
    }

    public String getOverrideReasonCode() {
        return overrideReasonCode;
    }

    public void setOverrideReasonCode(String overrideReasonCode) {
        this.overrideReasonCode = overrideReasonCode;
    }

    public BigDecimal getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(BigDecimal taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(BigDecimal taxableAmount) {
        this.taxableAmount = taxableAmount;
    }



}
