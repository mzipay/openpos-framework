package org.jumpmind.jumppos.domain.currency;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Embeddable
public class ExchangeRateId implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToOne
    private Currency fromCurrency;
    @OneToOne
    private Currency toCurrency;

    public Currency getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(Currency fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public Currency getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(Currency toCurrency) {
        this.toCurrency = toCurrency;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        ExchangeRateId rhs = (ExchangeRateId) obj;
        return new EqualsBuilder().appendSuper(super.equals(obj)).append(fromCurrency, rhs.fromCurrency).append(
                toCurrency, rhs.toCurrency).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(13, 41).append(fromCurrency).append(toCurrency).toHashCode();
    }
}
