package org.jumpmind.pos.tax.model;

import java.math.BigDecimal;

public class TaxableItem {

    private int sequenceNumber;

    private String taxGroupId;

    private BigDecimal extendedAmount = BigDecimal.ZERO;

    private boolean taxExemptEligible = false;

    public TaxableItem() {
    }

    public TaxableItem(int sequenceNumber, String taxGroupId, BigDecimal extendedAmount, boolean taxExemptEligible) {
        this.sequenceNumber = sequenceNumber;
        this.taxGroupId = taxGroupId;
        this.extendedAmount = extendedAmount;
        this.taxExemptEligible = taxExemptEligible;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getTaxGroupId() {
        return taxGroupId;
    }

    public void setTaxGroupId(String taxGroupId) {
        this.taxGroupId = taxGroupId;
    }

    public BigDecimal getExtendedAmount() {
        return extendedAmount;
    }

    public void setExtendedAmount(BigDecimal extendedAmount) {
        this.extendedAmount = extendedAmount;
    }

    public boolean isTaxExemptEligible() {
        return taxExemptEligible;
    }

    public void setTaxExemptEligible(boolean taxExemptEligible) {
        this.taxExemptEligible = taxExemptEligible;
    }

}
