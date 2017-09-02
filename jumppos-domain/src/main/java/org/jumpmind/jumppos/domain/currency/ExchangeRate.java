package org.jumpmind.jumppos.domain.currency;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.jumpmind.jumppos.domain.BaseEntity;

@Entity
public class ExchangeRate extends BaseEntity {

    @Id
    private ExchangeRateId id;
    private BigDecimal rate;
    private BigDecimal serviceFee;
    private BigDecimal minimumCurrencyAmount;

    public ExchangeRateId getId() {
        return id;
    }

    public void setId(ExchangeRateId id) {
        this.id = id;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public BigDecimal getMinimumCurrencyAmount() {
        return minimumCurrencyAmount;
    }

    public void setMinimumCurrencyAmount(BigDecimal minimumCurrencyAmount) {
        this.minimumCurrencyAmount = minimumCurrencyAmount;
    }

}
