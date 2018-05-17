package org.jumpmind.pos.tax.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Entity;
import org.jumpmind.pos.persist.Table;

/**
 * A government authority that levies sales taxes and on whose behalf the store
 * collects these sales taxes.
 * 
 * @author elong
 * 
 */
@Table(description = "A government authority that levies sales taxes and on whose behalf the store collects these sales taxes.")
public class TaxAuthority extends Entity implements Comparable<TaxAuthority> {

    @Column(primaryKey = true)
    private String id;

    @Column
    private String name;

    @Column
    private String roundingCode;

    @Column
    private Integer roundingDigitsQuantity;

    private Collection<TaxGroupRule> taxGroupRules;

    public TaxAuthority() {
    }

    public TaxAuthority(String id) {
        this.id = id;
    }

    public String toString() {
        return getClass().getSimpleName() + " " + id + ": " + name;
    }

    public boolean equals(Object o) {
        if (o != null && o instanceof TaxAuthority) {
            TaxAuthority taxAuthority = (TaxAuthority) o;
            return taxAuthority.getId().equals(id);
        }
        return false;
    }

    public int compareTo(TaxAuthority o) {
        if (o != null && o instanceof TaxAuthority) {
            TaxAuthority taxAuthority = (TaxAuthority) o;
            return taxAuthority.getId().compareTo(id);
        }
        return -1;
    }

    public void addTaxGroupRule(TaxGroupRule taxGroupRule) {
        if (taxGroupRules == null) {
            taxGroupRules = new ArrayList<TaxGroupRule>();
        }
        taxGroupRules.add(taxGroupRule);
    }

    public TaxGroupRule getTaxGroupRule(String taxableGroupId) {
        for (TaxGroupRule taxGroupRule : taxGroupRules) {
            if (taxGroupRule.getTaxableGroup().getId().equals(taxableGroupId)) {
                return taxGroupRule;
            }
        }
        return null;
    }

    public BigDecimal round(BigDecimal amount) {
        int digits = 2;
        if (roundingDigitsQuantity != null && roundingDigitsQuantity.intValue() >= 2) {
            digits = getRoundingDigitsQuantity();
        }

        RoundingMode mode = RoundingMode.HALF_UP;
        if (roundingCode != null) {
            if (roundingCode.equals(TaxConstants.ROUNDING_UP)) {
                mode = RoundingMode.HALF_UP;
            } else if (roundingCode.equals(TaxConstants.ROUNDING_DOWN)) {
                mode = RoundingMode.DOWN;
            } else if (roundingCode.equals(TaxConstants.ROUNDING_HALF_DOWN)) {
                mode = RoundingMode.HALF_DOWN;
            }
        }

        return amount.setScale(digits, mode);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoundingCode() {
        return roundingCode;
    }

    public void setRoundingCode(String roundingCode) {
        this.roundingCode = roundingCode;
    }

    public Integer getRoundingDigitsQuantity() {
        return roundingDigitsQuantity;
    }

    public void setRoundingDigitsQuantity(Integer roundingDigitsQuantity) {
        this.roundingDigitsQuantity = roundingDigitsQuantity;
    }

    public Collection<TaxGroupRule> getTaxGroupRules() {
        return taxGroupRules;
    }

    public void setTaxGroupRules(Collection<TaxGroupRule> taxGroupRules) {
        this.taxGroupRules = taxGroupRules;
    }

}
