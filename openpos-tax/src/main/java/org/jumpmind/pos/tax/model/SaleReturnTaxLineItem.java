package org.jumpmind.pos.tax.model;

import java.math.BigDecimal;

/**
 * A Line Item to record taxation implications of a single SALE RETURN LINE ITEM
 * rather than an entire RETAIL TRANSACTION.
 * 
 * @author elong
 * 
 */
public class SaleReturnTaxLineItem {

    private String id;

    private TaxAuthority taxAuthority;

    private TaxableGroup taxableGroup;

    private BigDecimal taxableAmount;

    private BigDecimal taxAmount;

    private BigDecimal taxPercent;

    private TaxExemptionModifier saleReturnTaxExemptionModifier;

    private TaxOverrideModifier saleReturnTaxOverrideModifier;

    private SaleReturnLineItem lineItem;

    public SaleReturnTaxLineItem() {
    }

    public SaleReturnTaxLineItem(TaxAuthority taxAuthority, TaxableGroup taxableGroup, BigDecimal taxableAmount, BigDecimal taxAmount,
            BigDecimal taxPercent) {
        this.taxAuthority = taxAuthority;
        this.taxableGroup = taxableGroup;
        this.taxableAmount = taxableAmount;
        this.taxAmount = taxAmount;
        this.taxPercent = taxPercent;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public TaxExemptionModifier getSaleReturnTaxExemptionModifier() {
        return saleReturnTaxExemptionModifier;
    }

    public void setTaxExemptionModifier(TaxExemptionModifier saleReturnTaxExemptionModifier) {
        this.saleReturnTaxExemptionModifier = saleReturnTaxExemptionModifier;
    }

    public TaxOverrideModifier getTaxOverrideModifier() {
        return saleReturnTaxOverrideModifier;
    }

    public void setSaleReturnTaxOverrideModifier(TaxOverrideModifier saleReturnTaxOverrideModifier) {
        this.saleReturnTaxOverrideModifier = saleReturnTaxOverrideModifier;
    }

    public BigDecimal getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(BigDecimal taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public TaxableGroup getTaxableGroup() {
        return taxableGroup;
    }

    public void setTaxableGroup(TaxableGroup taxableGroup) {
        this.taxableGroup = taxableGroup;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public TaxAuthority getTaxAuthority() {
        return taxAuthority;
    }

    public void setTaxAuthority(TaxAuthority taxAuthority) {
        this.taxAuthority = taxAuthority;
    }

    public BigDecimal getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(BigDecimal taxPercent) {
        this.taxPercent = taxPercent;
    }

    public void setLineItem(SaleReturnLineItem lineItem) {
        this.lineItem = lineItem;
    }

    public SaleReturnLineItem getLineItem() {
        return lineItem;
    }

}
