package org.jumpmind.jumppos.domain.currency;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

import org.jumpmind.jumppos.domain.BaseEntity;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Denomination extends BaseEntity {

    @Id
    private String id;

    @OneToOne
    private Currency currency;

    private BigDecimal value;

    /**
     * Designates whether currency is in common circulation.
     */
    private boolean common;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public boolean isCommon() {
        return common;
    }

    public void setCommon(boolean common) {
        this.common = common;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

}
