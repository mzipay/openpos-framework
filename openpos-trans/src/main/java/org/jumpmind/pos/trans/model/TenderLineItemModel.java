package org.jumpmind.pos.trans.model;

import java.math.BigDecimal;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Table;

@Table(name="sale_line_item")
public class TenderLineItemModel extends AbstractTransLineModel {

    private static final long serialVersionUID = 1L;
    
    @Column
    String tenderType;
    
    @Column
    String tenderSubType;
    
    @Column
    String reasonCode;
    
    @Column
    boolean isChange;
    
    @Column
    String customerAccountNumber;
    
    @Column
    String tenderAccountNumber;
    
    @Column
    String currencyId;
    
    @Column
    BigDecimal exchangeRate;
    
    @Column
    BigDecimal tenderAmount;
    
    @Column
    BigDecimal cashBackAmount;

    public String getTenderType() {
        return tenderType;
    }

    public void setTenderType(String tenderType) {
        this.tenderType = tenderType;
    }

    public String getTenderSubType() {
        return tenderSubType;
    }

    public void setTenderSubType(String tenderSubType) {
        this.tenderSubType = tenderSubType;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public boolean isChange() {
        return isChange;
    }

    public void setChange(boolean isChange) {
        this.isChange = isChange;
    }

    public String getCustomerAccountNumber() {
        return customerAccountNumber;
    }

    public void setCustomerAccountNumber(String customerAccountNumber) {
        this.customerAccountNumber = customerAccountNumber;
    }

    public String getTenderAccountNumber() {
        return tenderAccountNumber;
    }

    public void setTenderAccountNumber(String tenderAccountNumber) {
        this.tenderAccountNumber = tenderAccountNumber;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getTenderAmount() {
        return tenderAmount;
    }

    public void setTenderAmount(BigDecimal tenderAmount) {
        this.tenderAmount = tenderAmount;
    }

    public BigDecimal getCashBackAmount() {
        return cashBackAmount;
    }

    public void setCashBackAmount(BigDecimal cashBackAmount) {
        this.cashBackAmount = cashBackAmount;
    }
    
    
    
}
