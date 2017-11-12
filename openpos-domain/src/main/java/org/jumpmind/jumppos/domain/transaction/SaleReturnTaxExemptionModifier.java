package org.jumpmind.jumppos.domain.transaction;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.jumpmind.jumppos.domain.BaseEntity;

/**
 * A line item modifier to the SaleReturnTaxLineItem component of a
 * RetailTransaction that provides supplementary data regarding tax exemptions.
 * 
 * @author elong
 * 
 */
@Entity
public class SaleReturnTaxExemptionModifier extends BaseEntity  {
    
    @Id
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
