package org.jumpmind.pos.tax.model;

import java.math.BigDecimal;

/**
 * A line item modifier to the TaxLineItem component of a RetailTransaction that
 * provides supplementary data regarding tax exemptions.
 * 
 * @author elong
 * 
 */
public class TaxExemptionModifier {

    private String id;

    private BigDecimal exemptTaxableAmount;

    private BigDecimal exemptTaxAmount;

    private String reasonCode;

    private String certificateNumber;

    private String certificateHolderName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCertificateHolderName() {
        return certificateHolderName;
    }

    public void setCertificateHolderName(String certificateHolderName) {
        this.certificateHolderName = certificateHolderName;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public BigDecimal getExemptTaxableAmount() {
        return exemptTaxableAmount;
    }

    public void setExemptTaxableAmount(BigDecimal exemptTaxableAmount) {
        this.exemptTaxableAmount = exemptTaxableAmount;
    }

    public BigDecimal getExemptTaxAmount() {
        return exemptTaxAmount;
    }

    public void setExemptTaxAmount(BigDecimal exemptTaxAmount) {
        this.exemptTaxAmount = exemptTaxAmount;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

}
