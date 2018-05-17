package org.jumpmind.pos.tax.model;

import java.math.BigDecimal;

public class ItemTaxAmount extends TaxAmount {

    private int sequenceNumber;

    public ItemTaxAmount() {
    }

    public ItemTaxAmount(int sequenceNumber, String taxAuthorityId, String taxGroupId, BigDecimal taxableAmount, BigDecimal taxAmount,
            BigDecimal taxPercent) {
        super(taxAuthorityId, taxGroupId, taxableAmount, taxAmount, taxPercent);
        this.sequenceNumber = sequenceNumber;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

}
