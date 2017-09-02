package org.jumpmind.jumppos.domain.transaction;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.jumpmind.jumppos.domain.currency.Currency;

@Entity
public class TenderLineItem extends RetailTransactionLineItem {

    private Integer tenderType;
    private BigDecimal tenderAmount;
    private BigDecimal foreignCurrencyAmount;
    @OneToOne
    private Currency foreignCurrency;
    private BigDecimal exchangeRate;
    private BigDecimal tenderAmountAppliedToTransactoin;
    private BigDecimal tipAmount;
    private BigDecimal cashBackAmount;

    public Integer getTenderType() {
        return tenderType;
    }

    public void setTenderType(Integer tenderType) {
        this.tenderType = tenderType;
    }

    public BigDecimal getTenderAmount() {
        return tenderAmount;
    }

    public void setTenderAmount(BigDecimal tenderAmount) {
        this.tenderAmount = tenderAmount;
    }

    public BigDecimal getForeignCurrencyAmount() {
        return foreignCurrencyAmount;
    }

    public void setForeignCurrencyAmount(BigDecimal foreignCurrencyAmount) {
        this.foreignCurrencyAmount = foreignCurrencyAmount;
    }

    public Currency getForeignCurrency() {
        return foreignCurrency;
    }

    public void setForeignCurrency(Currency foreignCurrency) {
        this.foreignCurrency = foreignCurrency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getTenderAmountAppliedToTransactoin() {
        return tenderAmountAppliedToTransactoin;
    }

    public void setTenderAmountAppliedToTransactoin(BigDecimal tenderAmountAppliedToTransactoin) {
        this.tenderAmountAppliedToTransactoin = tenderAmountAppliedToTransactoin;
    }

    public BigDecimal getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(BigDecimal tipAmount) {
        this.tipAmount = tipAmount;
    }

    public BigDecimal getCashBackAmount() {
        return cashBackAmount;
    }

    public void setCashBackAmount(BigDecimal cashBackAmount) {
        this.cashBackAmount = cashBackAmount;
    }

}
