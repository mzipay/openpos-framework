package org.jumpmind.jumppos.domain.item;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.jumpmind.jumppos.domain.BaseEntity;
import org.jumpmind.jumppos.domain.tax.TaxAuthority;
import org.jumpmind.jumppos.domain.tax.TaxableGroup;

/**
 * An association of a StockItem and a ReturnAgent that defines the rules
 * governing the deposit payment that must be paid by the customer at the time
 * the item is purchased and the refund that must be made to the customer upon
 * return of the item package or container. This rule is most often related to
 * bottles, aluminum cans, crates and other containers which must be returned
 * for reuse or recycling.
 */
@Entity
public class DepositRule extends BaseEntity {

    @Id
    private String id;

    /**
     * The monetary amount of the Deposit that is paid to or collected from the
     * Customer at purchase and return.
     */
    private BigDecimal depositAmount;
    private Boolean taxableFlag;
    private String description;
    @OneToOne
    private TaxAuthority taxAuthority;
    @OneToOne
    private TaxableGroup taxGroup;

    public DepositRule() {
    }

    public DepositRule(BigDecimal depositAmount, boolean taxable, String description) {
        this.depositAmount = depositAmount;
        this.taxableFlag = taxable;
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public Boolean isTaxableFlag() {
        return taxableFlag;
    }

    public void setTaxableFlag(Boolean taxable) {
        this.taxableFlag = taxable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaxAuthority getTaxAuthority() {
        return taxAuthority;
    }

    public void setTaxAuthority(TaxAuthority taxAuthority) {
        this.taxAuthority = taxAuthority;
    }

    public TaxableGroup getTaxGroup() {
        return taxGroup;
    }

    public void setTaxGroup(TaxableGroup taxGroup) {
        this.taxGroup = taxGroup;
    }

}
