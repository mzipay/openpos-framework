package org.jumpmind.jumppos.domain.item;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.jumpmind.jumppos.domain.BaseEntity;

/**
 * A set of commonly used selling rules for Items. Usually this entity will be
 * in a one-to-one relationship with Item, except when each combination of
 * Size/Color/Style of a particular piece of merchandise are individually
 * assigned to a new SKU for inventory recording purposes but all
 * Sizes/Colors/Styles of that item have the same selling rules.
 */
@Entity
public class ItemSellingRule extends BaseEntity {

    @Id
    private Long id;
    private String itemTenderRestrictionGroupCode;
    private Integer manufacturerID;
    private Integer manufacturerFamilyCode;
    @OneToOne
    private DepositRule depositRule;
    private Boolean couponRestrictedFlag;
    private Boolean priceEntryRequiredFlag;
    private Boolean employeeDiscountAllowedFlag;
    private Boolean prohibitQuantityKeyFlag;
    private Boolean allowFoodStampFlag;
    private Integer maximumSaleUnitCount;
    private Boolean prohibitReturnFlag;

    public ItemSellingRule() {

    }

    public ItemSellingRule(String itemTenderRestrictionGroupCode,
            Integer manufacturerID, Integer manufacturerFamilyCode,
            DepositRule depositRule, Boolean couponRestrictedFlag,
            Boolean priceEntryRequiredFlag,
            Boolean employeeDiscountAllowedFlag, Boolean allowFoodStampFlag,
            Integer maximumSaleUnitCount, Boolean prohibitReturnFlag,
            Boolean prohibitQuantityKeyFlag) {
        this.itemTenderRestrictionGroupCode = itemTenderRestrictionGroupCode;
        this.manufacturerID = manufacturerID;
        this.manufacturerFamilyCode = manufacturerFamilyCode;
        this.depositRule = depositRule;
        this.couponRestrictedFlag = couponRestrictedFlag;
        this.priceEntryRequiredFlag = priceEntryRequiredFlag;
        this.employeeDiscountAllowedFlag = employeeDiscountAllowedFlag;
        this.allowFoodStampFlag = allowFoodStampFlag;
        this.maximumSaleUnitCount = maximumSaleUnitCount;
        this.prohibitReturnFlag = prohibitReturnFlag;
        this.prohibitQuantityKeyFlag = prohibitQuantityKeyFlag;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getId() {
        return id;
    }

    /**
     * @return A actionCode to denote a specific group of ItemTenderRestrictions which
     *         are part of the SellingRule.
     */
    public String getItemTenderRestrictionGroupCode() {
        return itemTenderRestrictionGroupCode;
    }

    /**
     * @param itemTenderRestrictionGroupCode
     *            A actionCode to denote a specific group of ItemTenderRestrictions
     *            which are part of the SellingRule.
     */
    public void setItemTenderRestrictionGroupCode(
            String itemTenderRestrictionGroupCode) {
        this.itemTenderRestrictionGroupCode = itemTenderRestrictionGroupCode;
    }

    /**
     * @return A unique identifier to denote the ITEM MANUFACTURER.
     */
    public Integer getManufacturerID() {
        return manufacturerID;
    }

    /**
     * @param manufacturerID
     *            A unique identifier to denote the ITEM MANUFACTURER.
     */
    public void setManufacturerID(Integer manufacturerID) {
        this.manufacturerID = manufacturerID;
    }

    /**
     * @return The UPC designated three digit coupon family actionCode for the
     *         manufacturer item.
     */
    public Integer getManufacturerFamilyCode() {
        return manufacturerFamilyCode;
    }

    /**
     * @param manufacturerFamilyCode
     *            The UPC designated three digit coupon family actionCode for the
     *            manufacturer item.
     */
    public void setManufacturerFamilyCode(Integer manufacturerFamilyCode) {
        this.manufacturerFamilyCode = manufacturerFamilyCode;
    }

    /**
     * @return A flag to indicate whether a coupon can be redeemed for this ITEM
     */
    public Boolean isCouponRestrictedFlag() {
        return couponRestrictedFlag;
    }

    /**
     * @param couponRestrictedFlag
     *            A flag to indicate whether a coupon can be redeemed for this
     *            ITEM
     */
    public void setCouponRestrictedFlag(Boolean couponRestrictedFlag) {
        this.couponRestrictedFlag = couponRestrictedFlag;
    }

    /**
     * @return A flag to denote that the retail price is to be manually entered
     *         when this ITEM is sold.
     */
    public Boolean isPriceEntryRequiredFlag() {
        return priceEntryRequiredFlag;
    }

    /**
     * @param priceEntryRequiredFlag
     *            A flag to denote that the retail price is to be manually
     *            entered when this ITEM is sold.
     */
    public void setPriceEntryRequiredFlag(Boolean priceEntryRequiredFlag) {
        this.priceEntryRequiredFlag = priceEntryRequiredFlag;
    }

    /**
     * @return A flag that enables or disables price derivation rule eligibility
     *         to all employees for this Item.
     */
    public Boolean isEmployeeDiscountAllowedFlag() {
        return employeeDiscountAllowedFlag;
    }

    /**
     * @param employeeDiscountAllowedFlag
     *            A flag that enables or disables price derivation rule
     *            eligibility to all employees for this Item.
     */
    public void setEmployeeDiscountAllowedFlag(
            Boolean employeeDiscountAllowedFlag) {
        this.employeeDiscountAllowedFlag = employeeDiscountAllowedFlag;
    }

    /**
     * @return A flag to inicate whether food stamps can be tendered for this
     *         Item.
     */
    public Boolean isAllowFoodStampFlag() {
        return allowFoodStampFlag;
    }

    /**
     * @param allowFoodStampFlag
     *            A flag to inicate whether food stamps can be tendered for this
     *            Item.
     */
    public void setAllowFoodStampFlag(Boolean allowFoodStampFlag) {
        this.allowFoodStampFlag = allowFoodStampFlag;
    }

    /**
     * @return Defines the maximum number of retail sale units that may be
     *         purchased in a single RetailTransaction. Typically this is used
     *         for "rationed " merchandise.
     */
    public Integer getMaximumSaleUnitCount() {
        return maximumSaleUnitCount;
    }

    /**
     * @param maximumSaleUnitCount
     *            Defines the maximum number of retail sale units that may be
     *            purchased in a single RetailTransaction. Typically this is
     *            used for "rationed " merchandise.
     */
    public void setMaximumSaleUnitCount(Integer maximumSaleUnitCount) {
        this.maximumSaleUnitCount = maximumSaleUnitCount;
    }

    /**
     * @return A flag to denote whether or not this item may be returned. (Eg:
     *         Freezer or Chilled merchandise)
     */
    public Boolean isProhibitReturnFlag() {
        return prohibitReturnFlag;
    }

    /**
     * @param prohibitReturnFlag
     *            A flag to denote whether or not this item may be returned.
     *            (Eg: Freezer or Chilled merchandise)
     */
    public void setProhibitReturnFlag(Boolean prohibitReturnFlag) {
        this.prohibitReturnFlag = prohibitReturnFlag;
    }

    /**
     * @return A flag denoting that the Quantity key may not be used when
     *         selling this Item.
     */
    public Boolean getProhibitQuantityKeyFlag() {
        return prohibitQuantityKeyFlag;
    }

    /**
     * @param prohibitQuantityKeyFlag
     *            A flag denoting that the Quantity key may not be used when
     *            selling this Item.
     */
    public void setProhibitQuantityKeyFlag(Boolean prohibitQuantityKeyFlag) {
        this.prohibitQuantityKeyFlag = prohibitQuantityKeyFlag;
    }

    public DepositRule getDepositRule() {
        return depositRule;
    }

    public void setDepositRule(DepositRule depositRule) {
        this.depositRule = depositRule;
    }
}
