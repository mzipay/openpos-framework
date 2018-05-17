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

}
