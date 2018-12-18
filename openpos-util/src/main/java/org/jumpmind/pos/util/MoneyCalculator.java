package org.jumpmind.pos.util;

import java.math.BigDecimal;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.money.MoneyUtils;

public class MoneyCalculator {

    CurrencyUnit currency;
    
    public MoneyCalculator(String isoCurrencyCode) {
        this.currency = CurrencyUnit.of(isoCurrencyCode);
    }
    
    public BigDecimal zero() {
        return Money.zero(currency).getAmount();
    }
    
    
    public BigDecimal amount(BigDecimal amount) {
        return money(amount).getAmount();
    }
    
    public Money money(BigDecimal amount) {
        if (amount != null) {
            return Money.of(currency, amount);
        } else {
            return Money.zero(currency);
        }
    }
    
    public BigDecimal add(BigDecimal amount1, BigDecimal amount2) {
        return MoneyUtils.add(money(amount1), money(amount2)).getAmount();
    }
    
}
