package org.jumpmind.pos.tax.model;

import java.math.BigDecimal;

/**
 * A line item modifier to the TaxLineItem component of a RetailTransaction that
 * provides supplementary data regarding tax overrides. (Where the amount of tax
 * collected is reduced rather than exempted).
 * 
 * @author elong
 * 
 */
public class TaxOverrideModifier {

    private String id;

    private BigDecimal originalTaxAmount;

    private BigDecimal newTaxAmount;

    private BigDecimal originalTaxPercent;

    private BigDecimal newTaxPercent;

    private String reasonCode;

    private String certificateNumber;

    private String certificateHolderName;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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

    public BigDecimal getNewTaxAmount() {
        return newTaxAmount;
    }

    public void setNewTaxAmount(BigDecimal newTaxAmount) {
        this.newTaxAmount = newTaxAmount;
    }

    public BigDecimal getNewTaxPercent() {
        return newTaxPercent;
    }

    public void setNewTaxPercent(BigDecimal newTaxPercent) {
        this.newTaxPercent = newTaxPercent;
    }

    public BigDecimal getOriginalTaxAmount() {
        return originalTaxAmount;
    }

    public void setOriginalTaxAmount(BigDecimal originalTaxAmount) {
        this.originalTaxAmount = originalTaxAmount;
    }

    public BigDecimal getOriginalTaxPercent() {
        return originalTaxPercent;
    }

    public void setOriginalTaxPercent(BigDecimal originalTaxPercent) {
        this.originalTaxPercent = originalTaxPercent;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

}
