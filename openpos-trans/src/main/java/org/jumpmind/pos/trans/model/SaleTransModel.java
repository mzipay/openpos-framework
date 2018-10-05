package org.jumpmind.pos.trans.model;

import java.math.BigDecimal;
import java.util.Date;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Table;

@Table(name = "sale_trans")
public class SaleTransModel extends AbstractTransModel {

    private static final long serialVersionUID = 1L;

    @Column
    BigDecimal total;

    @Column
    BigDecimal subtotal;

    @Column
    BigDecimal taxTotal;

    @Column
    int unitCount;

    @Column
    String tillId;

    @Column
    int ringElapsedTimeInSecs;

    @Column
    int tenderElapsedTimeInSecs;

    @Column
    int idleElapsedTimeInSecs;

    @Column
    int lockElapsedTimeInSecs;

    @Column
    Date receiptDateTime;

    @Column
    String customerId;

    @Column
    CustomerIdMethodCode customerIdMethodCode;

    @Column
    CustomerIdTypeCode dustomerIdTypeCode;

    @Column
    String sellingChannelId;

    @Column
    TransactionEntryModeCode entryModeCode;
    
    @Column 
    String canceledReasonCode;
    
    @Column
    String loyaltyCardNumber;
    
    @Column
    String taxExemptionId;

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public int getUnitCount() {
        return unitCount;
    }

    public void setUnitCount(int unitCount) {
        this.unitCount = unitCount;
    }

    public String getTillId() {
        return tillId;
    }

    public void setTillId(String tillId) {
        this.tillId = tillId;
    }

    public int getRingElapsedTimeInSecs() {
        return ringElapsedTimeInSecs;
    }

    public void setRingElapsedTimeInSecs(int ringElapsedTimeInSecs) {
        this.ringElapsedTimeInSecs = ringElapsedTimeInSecs;
    }

    public int getTenderElapsedTimeInSecs() {
        return tenderElapsedTimeInSecs;
    }

    public void setTenderElapsedTimeInSecs(int tenderElapsedTimeInSecs) {
        this.tenderElapsedTimeInSecs = tenderElapsedTimeInSecs;
    }

    public int getIdleElapsedTimeInSecs() {
        return idleElapsedTimeInSecs;
    }

    public void setIdleElapsedTimeInSecs(int idleElapsedTimeInSecs) {
        this.idleElapsedTimeInSecs = idleElapsedTimeInSecs;
    }

    public int getLockElapsedTimeInSecs() {
        return lockElapsedTimeInSecs;
    }

    public void setLockElapsedTimeInSecs(int lockElapsedTimeInSecs) {
        this.lockElapsedTimeInSecs = lockElapsedTimeInSecs;
    }

    public Date getReceiptDateTime() {
        return receiptDateTime;
    }

    public void setReceiptDateTime(Date receiptDateTime) {
        this.receiptDateTime = receiptDateTime;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public CustomerIdMethodCode getCustomerIdMethodCode() {
        return customerIdMethodCode;
    }

    public void setCustomerIdMethodCode(CustomerIdMethodCode customerIdMethodCode) {
        this.customerIdMethodCode = customerIdMethodCode;
    }

    public CustomerIdTypeCode getDustomerIdTypeCode() {
        return dustomerIdTypeCode;
    }

    public void setDustomerIdTypeCode(CustomerIdTypeCode dustomerIdTypeCode) {
        this.dustomerIdTypeCode = dustomerIdTypeCode;
    }

    public String getSellingChannelId() {
        return sellingChannelId;
    }

    public void setSellingChannelId(String sellingChannelId) {
        this.sellingChannelId = sellingChannelId;
    }

    public TransactionEntryModeCode getEntryModeCode() {
        return entryModeCode;
    }

    public void setEntryModeCode(TransactionEntryModeCode entryModeCode) {
        this.entryModeCode = entryModeCode;
    }

    public void setTaxTotal(BigDecimal taxTotal) {
        this.taxTotal = taxTotal;
    }

    public BigDecimal getTaxTotal() {
        return taxTotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setLoyaltyCardNumber(String loyaltyCardNumber) {
        this.loyaltyCardNumber = loyaltyCardNumber;
    }
    
    public String getLoyaltyCardNumber() {
        return loyaltyCardNumber;
    }
    
    public void setTaxExemptionId(String taxExemptionId) {
        this.taxExemptionId = taxExemptionId;
    }
    
    public String getTaxExemptionId() {
        return taxExemptionId;
    }

}
