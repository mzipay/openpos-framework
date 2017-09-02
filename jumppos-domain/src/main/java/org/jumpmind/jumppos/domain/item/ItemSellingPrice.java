package org.jumpmind.jumppos.domain.item;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.jumpmind.jumppos.domain.BaseEntity;
import org.jumpmind.jumppos.domain.business.BusinessUnitGroup;

@Entity
public class ItemSellingPrice extends BaseEntity {

    @Id
    private String id;

    @Embedded
    private BusinessUnitGroupItemId businessUnitGroupItemId;

    /**
     * The current retail price per sale unit of the item. This monetary amount
     * is used as the basis for deriving retail price at the poInteger of sale.
     */
    private BigDecimal unitPrice;

    /**
     * The date on which the item-current-sale-retail-price-amount becomes
     * effective and is used as the basis for deriving the retail price used at
     * the poInteger of sale.
     */
    private Date effectiveStartTime;

    /**
     * The date the item-current-sale-unit-retail-price-amount expires and is no
     * longer valid as the basis for deriving the poInteger of sale price.
     */
    private Date effectiveEndTime;

    /**
     * Indicates if the prices include all applicable sales taxes or not.
     */
    private Boolean includeSalesTaxFlag = true;

    public ItemSellingPrice() {
    }
    
    public ItemSellingPrice(String id, Item item, BusinessUnitGroup group) {
        this.id = id;
        this.businessUnitGroupItemId = new BusinessUnitGroupItemId(item, group);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public BusinessUnitGroupItemId getBusinessUnitGroupItemId() {
        return businessUnitGroupItemId;
    }

    public void setBusinessUnitGroupItemId(BusinessUnitGroupItemId group) {
        this.businessUnitGroupItemId = group;
    }

    public Item getItem() {
        return businessUnitGroupItemId != null ? businessUnitGroupItemId.getItem() : null;
    }


    public BusinessUnitGroup getBusinessUnitGroup() {
        return businessUnitGroupItemId != null ? businessUnitGroupItemId.getBusinessUnitGroup() : null;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal currentSaleUnitRetailPriceAmount) {
        this.unitPrice = currentSaleUnitRetailPriceAmount;
    }

    public void setIncludeSalesTaxFlag(Boolean includeSalesTaxFlag) {
        this.includeSalesTaxFlag = includeSalesTaxFlag;
    }

    public Boolean getIncludeSalesTaxFlag() {
        return includeSalesTaxFlag;
    }
    
    public void setEffectiveEndTime(Date effectiveEndTime) {
        this.effectiveEndTime = effectiveEndTime;
    }
    
    public Date getEffectiveEndTime() {
        return effectiveEndTime;
    }
    
    public void setEffectiveStartTime(Date effectiveStartTime) {
        this.effectiveStartTime = effectiveStartTime;
    }
    
    public Date getEffectiveStartTime() {
        return effectiveStartTime;
    }
}
