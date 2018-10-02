package org.jumpmind.pos.item.model;

import java.util.Date;

import org.jumpmind.pos.persist.AbstractModel;
import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Table;

@Table(name="selling_rule")
public class SellingRuleModel extends AbstractModel {

    private static final long serialVersionUID = 1L;

    @Column(primaryKey=true)
    String sellingRuleId;
    
    @Column
    String sellingStatusCode;

    @Column
    Date effectiveStartTime;
    
    @Column
    Date effectiveEndTime;
    
    @Column
    boolean couponRestricted;
    
    @Column
    boolean eletronicCouponRestricted;
    
    @Column
    boolean priceEntryRequired;
    
    @Column
    boolean weightEntryRequired;
    
    @Column
    boolean employeeDiscountAllowed;
    
    @Column
    boolean foodStampAllowed;
    
    @Column
    boolean couponMultiplyAllowed;
    
    @Column
    boolean repeatKeyProhibitted;
    
    @Column
    boolean returnProhibitted;
    
    @Column
    boolean frequentShopperEligible;
    
    @Column
    boolean wicAllowed;
    
    @Column
    boolean raincheckEligible;
    
    @Column
    boolean giveawayEligble;
    
    @Column
    int frequentShopperPoints;
    
    @Column
    int minimumSaleUnitCount;
    
    @Column
    int maximumSaleUnitCount;

    public String getSellingRuleId() {
        return sellingRuleId;
    }

    public void setSellingRuleId(String sellingRuleId) {
        this.sellingRuleId = sellingRuleId;
    }

    public String getSellingStatusCode() {
        return sellingStatusCode;
    }

    public void setSellingStatusCode(String sellingStatusCode) {
        this.sellingStatusCode = sellingStatusCode;
    }

    public Date getEffectiveStartTime() {
        return effectiveStartTime;
    }

    public void setEffectiveStartTime(Date effectiveStartTime) {
        this.effectiveStartTime = effectiveStartTime;
    }

    public Date getEffectiveEndTime() {
        return effectiveEndTime;
    }

    public void setEffectiveEndTime(Date effectiveEndTime) {
        this.effectiveEndTime = effectiveEndTime;
    }

    public boolean isCouponRestricted() {
        return couponRestricted;
    }

    public void setCouponRestricted(boolean couponRestricted) {
        this.couponRestricted = couponRestricted;
    }

    public boolean isEletronicCouponRestricted() {
        return eletronicCouponRestricted;
    }

    public void setEletronicCouponRestricted(boolean eletronicCouponRestricted) {
        this.eletronicCouponRestricted = eletronicCouponRestricted;
    }

    public boolean isPriceEntryRequired() {
        return priceEntryRequired;
    }

    public void setPriceEntryRequired(boolean priceEntryRequired) {
        this.priceEntryRequired = priceEntryRequired;
    }

    public boolean isWeightEntryRequired() {
        return weightEntryRequired;
    }

    public void setWeightEntryRequired(boolean weightEntryRequired) {
        this.weightEntryRequired = weightEntryRequired;
    }

    public boolean isEmployeeDiscountAllowed() {
        return employeeDiscountAllowed;
    }

    public void setEmployeeDiscountAllowed(boolean employeeDiscountAllowed) {
        this.employeeDiscountAllowed = employeeDiscountAllowed;
    }

    public boolean isFoodStampAllowed() {
        return foodStampAllowed;
    }

    public void setFoodStampAllowed(boolean foodStampAllowed) {
        this.foodStampAllowed = foodStampAllowed;
    }

    public boolean isCouponMultiplyAllowed() {
        return couponMultiplyAllowed;
    }

    public void setCouponMultiplyAllowed(boolean couponMultiplyAllowed) {
        this.couponMultiplyAllowed = couponMultiplyAllowed;
    }

    public boolean isRepeatKeyProhibitted() {
        return repeatKeyProhibitted;
    }

    public void setRepeatKeyProhibitted(boolean repeatKeyProhibitted) {
        this.repeatKeyProhibitted = repeatKeyProhibitted;
    }

    public boolean isReturnProhibitted() {
        return returnProhibitted;
    }

    public void setReturnProhibitted(boolean returnProhibitted) {
        this.returnProhibitted = returnProhibitted;
    }

    public boolean isFrequentShopperEligible() {
        return frequentShopperEligible;
    }

    public void setFrequentShopperEligible(boolean frequentShopperEligible) {
        this.frequentShopperEligible = frequentShopperEligible;
    }

    public boolean isWicAllowed() {
        return wicAllowed;
    }

    public void setWicAllowed(boolean wicAllowed) {
        this.wicAllowed = wicAllowed;
    }

    public boolean isRaincheckEligible() {
        return raincheckEligible;
    }

    public void setRaincheckEligible(boolean raincheckEligible) {
        this.raincheckEligible = raincheckEligible;
    }

    public boolean isGiveawayEligble() {
        return giveawayEligble;
    }

    public void setGiveawayEligble(boolean giveawayEligble) {
        this.giveawayEligble = giveawayEligble;
    }

    public int getFrequentShopperPoints() {
        return frequentShopperPoints;
    }

    public void setFrequentShopperPoints(int frequentShopperPoints) {
        this.frequentShopperPoints = frequentShopperPoints;
    }

    public int getMinimumSaleUnitCount() {
        return minimumSaleUnitCount;
    }

    public void setMinimumSaleUnitCount(int minimumSaleUnitCount) {
        this.minimumSaleUnitCount = minimumSaleUnitCount;
    }

    public int getMaximumSaleUnitCount() {
        return maximumSaleUnitCount;
    }

    public void setMaximumSaleUnitCount(int maximumSaleUnitCount) {
        this.maximumSaleUnitCount = maximumSaleUnitCount;
    }
    
    
}
