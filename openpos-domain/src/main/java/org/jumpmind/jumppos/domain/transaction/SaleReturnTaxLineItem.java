package org.jumpmind.jumppos.domain.transaction;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.jumpmind.jumppos.domain.BaseEntity;
import org.jumpmind.jumppos.domain.tax.TaxAuthority;
import org.jumpmind.jumppos.domain.tax.TaxableGroup;

/**
 * A Line Item to record taxation implications of a single SALE RETURN LINE ITEM
 * rather than an entire RETAIL TRANSACTION.
 * 
 * @author elong
 * 
 */
@Entity
public class SaleReturnTaxLineItem extends BaseEntity {
    
    @Id
    private String id;

    @OneToOne
    private TaxAuthority taxAuthority;

    @OneToOne
    private TaxableGroup taxableGroup;

    private BigDecimal taxableAmount;

    private BigDecimal taxAmount;

    private BigDecimal taxPercent;

    @OneToOne
    private SaleReturnTaxExemptionModifier saleReturnTaxExemptionModifier;

    @OneToOne
    private SaleReturnTaxOverrideModifier saleReturnTaxOverrideModifier;
    
    @OneToOne
    private SaleReturnLineItem lineItem;

    public SaleReturnTaxLineItem() {
    }

    public SaleReturnTaxLineItem(TaxAuthority taxAuthority, TaxableGroup taxableGroup,
            BigDecimal taxableAmount, BigDecimal taxAmount, BigDecimal taxPercent) {
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
    
    public SaleReturnTaxExemptionModifier getSaleReturnTaxExemptionModifier() {
        return saleReturnTaxExemptionModifier;
    }

    public void setSaleReturnTaxExemptionModifier(
            SaleReturnTaxExemptionModifier saleReturnTaxExemptionModifier) {
        this.saleReturnTaxExemptionModifier = saleReturnTaxExemptionModifier;
    }

    public SaleReturnTaxOverrideModifier getSaleReturnTaxOverrideModifier() {
        return saleReturnTaxOverrideModifier;
    }

    public void setSaleReturnTaxOverrideModifier(
            SaleReturnTaxOverrideModifier saleReturnTaxOverrideModifier) {
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
