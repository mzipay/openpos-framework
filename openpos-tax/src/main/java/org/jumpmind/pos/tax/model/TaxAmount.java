package org.jumpmind.pos.tax.model;

import java.math.BigDecimal;

public class TaxAmount {

    private String taxAuthorityId;

    private String taxGroupId;

    private BigDecimal taxableAmount;

    private BigDecimal taxAmount;

    private BigDecimal taxPercent;

    public TaxAmount() {
    }

    public TaxAmount(String taxAuthorityId, String taxGroupId, BigDecimal taxableAmount, BigDecimal taxAmount, BigDecimal taxPercent) {
        this.taxAuthorityId = taxAuthorityId;
        this.taxGroupId = taxGroupId;
        this.taxableAmount = taxableAmount;
        this.taxAmount = taxAmount;
        this.taxPercent = taxPercent;
    }

    public TaxAmount(TaxAmount amount) {
        taxAuthorityId = amount.getTaxAuthorityId();
        taxGroupId = amount.getTaxGroupId();
        taxableAmount = amount.getTaxableAmount();
        taxAmount = amount.getTaxAmount();
        taxPercent = amount.getTaxPercent();
    }

    public String getTaxAuthorityId() {
        return taxAuthorityId;
    }

    public void setTaxAuthorityId(String taxAuthorityId) {
        this.taxAuthorityId = taxAuthorityId;
    }

    public String getTaxGroupId() {
        return taxGroupId;
    }

    public void setTaxGroupId(String taxGroupId) {
        this.taxGroupId = taxGroupId;
    }

    public BigDecimal getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(BigDecimal taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(BigDecimal taxPercent) {
        this.taxPercent = taxPercent;
    }

    public void add(TaxAmount itemTaxAmount) {
        if (taxableAmount == null) {
            taxableAmount = BigDecimal.ZERO;
        }
        if (taxAmount == null) {
            taxAmount = BigDecimal.ZERO;
        }
        taxableAmount = taxableAmount.add(itemTaxAmount.getTaxAmount());
        taxAmount = taxAmount.add(itemTaxAmount.getTaxAmount());
    }

}
