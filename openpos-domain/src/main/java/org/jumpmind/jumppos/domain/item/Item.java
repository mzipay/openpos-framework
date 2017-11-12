package org.jumpmind.jumppos.domain.item;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

import org.jumpmind.jumppos.domain.BaseEntity;
import org.jumpmind.jumppos.domain.tax.TaxableGroup;

/**
 * The lowest level of merchandise for which inventory and sales records are
 * retained within the retail store. It is analogous to the SKU ( Stock Keeping
 * Unit).
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_code", discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorValue("1")
public class Item extends BaseEntity {

    @Id
    private String id;

    @OneToOne
    private ItemSellingRule itemSellingRule;

    @OneToOne
    private PosDepartment posDepartment;

    @OneToOne
    private MerchandiseHierarchyGroup merchandiseHierarchyGroup;

    /**
     * A unique name to denote a class of ITEMs as a product of a single
     * supplier or manufacturer. The brand can include private label ITEMs.
     */
    private String brandName;

    /**
     * A flag to indicate that the RETAIL STORE is authorized to stock this
     * particular ITEM.
     */
    private Boolean authorizedForSaleFlag = true;
    private Boolean discountFlag = true;
    private Boolean taxExemptFlag = true;
    private String name;
    private String description;
    
    @OneToOne
    private TaxableGroup taxableGroup;

    public Item() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Boolean isAuthorizedForSaleFlag() {
        return authorizedForSaleFlag;
    }

    public void setAuthorizedForSaleFlag(Boolean authorizedForSaleFlag) {
        this.authorizedForSaleFlag = authorizedForSaleFlag;
    }

    public Boolean isDiscountFlag() {
        return discountFlag;
    }

    public void setDiscountFlag(Boolean discountFlag) {
        this.discountFlag = discountFlag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemSellingRule getItemSellingRule() {
        return itemSellingRule;
    }

    public void setItemSellingRule(ItemSellingRule itemSellingRule) {
        this.itemSellingRule = itemSellingRule;
    }

    public PosDepartment getPosDepartment() {
        return posDepartment;
    }

    public void setPosDepartment(PosDepartment posDepartment) {
        this.posDepartment = posDepartment;
    }

    public Boolean isTaxExemptFlag() {
        return taxExemptFlag;
    }

    public void setTaxExemptFlag(Boolean taxExemptable) {
        this.taxExemptFlag = taxExemptable;
    }

    public void setMerchandiseHierarchyGroup(MerchandiseHierarchyGroup merchandiseHierarchyGroup) {
        this.merchandiseHierarchyGroup = merchandiseHierarchyGroup;
    }

    public MerchandiseHierarchyGroup getMerchandiseHierarchyGroup() {
        return merchandiseHierarchyGroup;
    }

    public void setTaxableGroup(TaxableGroup taxableGroup) {
        this.taxableGroup = taxableGroup;
    }

    public TaxableGroup getTaxableGroup() {
        return taxableGroup;
    }

    public String toString() {
        return this.description;
    }
}
