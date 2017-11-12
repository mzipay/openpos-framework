package org.jumpmind.jumppos.domain.transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.jumpmind.jumppos.domain.currency.Money;

/**
 * A line item component of a RETAIL TRANSACTION that records the exchange in
 * ownership of a merchandise item (i.e. a sale or return) or the sale or refund
 * related to a service.
 */
@Entity
public class SaleReturnLineItem extends RetailTransactionLineItem {

    private String posItemId;

    private String itemId;
    
    private String description;

    private String posDepartmentId;

    private String taxGroupId;

    private ItemType itemType;

    /**
     * The regular or lookup per-unit price for the item before any discounts
     * have been applied.
     */
    private BigDecimal regularUnitPrice;

    /**
     * The actual per-unit price paid by the customer for this particular sale.
     * It is obtained by applying applicable price derivation rules to the
     * regular unit price.
     */
    private BigDecimal actualUnitPrice;

    /**
     * The number of units applicable to the regular or lookup-up price of the
     * Item at the time of the Transaction
     */
    private BigDecimal quantitySold = BigDecimal.ONE;

    private BigDecimal quantityReturned = BigDecimal.ZERO;

    private BigDecimal units = BigDecimal.ONE;

    /**
     * The product of multiplying {@link #quantity} by the
     * {@link #regularUnitPrice} derived from price lookup (and any applicable
     * price derivation rules). This retail sale unit price excludes sales
     * and/or value added tax.
     */
    private BigDecimal extendedAmount  = BigDecimal.ZERO;

    /**
     * The monetary total per-unit of all discounts and price modifiers that
     * were applied to this item
     */
    private BigDecimal unitDiscountAmount  = BigDecimal.ZERO;

    /**
     * The monetary total of all discounts and price modifiers that were applied
     * to this item
     */
    private BigDecimal extendedDiscountAmount = BigDecimal.ZERO;

    private ActionCode actionCode = ActionCode.SALE_ITEM;

    private EntryMethod entryMethod = EntryMethod.KEYED;

    private String reasonCode;

    private Boolean giftReceiptFlag = Boolean.FALSE;

    @OneToMany(mappedBy = "lineItem")
    private List<RetailPriceModifier> modifiers;

    @OneToMany(mappedBy = "lineItem")
    private List<RestrictionValidationModifier> restrictionValidationModifiers;

    @OneToMany(mappedBy = "lineItem")
    private List<SaleReturnTaxLineItem> saleReturnTaxLineItems;

    public void addSaleReturnTaxLineItem(SaleReturnTaxLineItem lineItem) {
        if (saleReturnTaxLineItems == null) {
            saleReturnTaxLineItems = new ArrayList<SaleReturnTaxLineItem>();
        }
        saleReturnTaxLineItems.add(lineItem);
    }

    public boolean isSale() {
        return actionCode != null && actionCode == ActionCode.SALE_ITEM;
    }

    public boolean isReturn() {
        return actionCode != null && actionCode == ActionCode.RETURN_ITEM;
    }

    public String getPosItemId() {
        return posItemId;
    }

    public void setPosItemId(String posItemId) {
        this.posItemId = posItemId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }

    public String getPosDepartmentId() {
        return posDepartmentId;
    }

    public void setPosDepartmentId(String posDepartmentId) {
        this.posDepartmentId = posDepartmentId;
    }

    public String getTaxGroupId() {
        return taxGroupId;
    }

    public void setTaxGroupId(String taxGroupId) {
        this.taxGroupId = taxGroupId;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public BigDecimal getRegularUnitPrice() {
        return regularUnitPrice;
    }

    public Money getRegularUnitPriceAsMoney()
    {
       return convertToMoney(getRegularUnitPrice());
    }
    
   
    public void setRegularUnitPrice(BigDecimal regularUnitPrice) {
        this.regularUnitPrice = regularUnitPrice;
    }

    public BigDecimal getActualUnitPrice() {
        return actualUnitPrice;
    }
    
    public Money getActualUnitPriceAsMoney()
    {
        return convertToMoney(getActualUnitPrice());
    }

    public void setActualUnitPrice(BigDecimal actualUnitPrice) {
        this.actualUnitPrice = actualUnitPrice;
    }

    public BigDecimal getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(BigDecimal quantitySold) {
        this.quantitySold = quantitySold;
    }

    public BigDecimal getQuantityReturned() {
        return quantityReturned;
    }

    public void setQuantityReturned(BigDecimal quantityReturned) {
        this.quantityReturned = quantityReturned;
    }

    public BigDecimal getUnits() {
        return units;
    }

    public void setUnits(BigDecimal units) {
        this.units = units;
    }

    public BigDecimal getExtendedAmount() {
        return extendedAmount;
    }
    
    public Money getExtendedAmountAsMoney()
    {
       return convertToMoney(getExtendedAmount());
    }
    

    public void setExtendedAmount(BigDecimal extendedAmount) {
        this.extendedAmount = extendedAmount;
    }

    public BigDecimal getUnitDiscountAmount() {
        return unitDiscountAmount;
    }
    
    public Money getUnitDiscountAmountAsMoney()
    {
       return convertToMoney(getUnitDiscountAmount());
    }

    public void setUnitDiscountAmount(BigDecimal unitDiscountAmount) {
        this.unitDiscountAmount = unitDiscountAmount;
    }

    public BigDecimal getExtendedDiscountAmount() {
        return extendedDiscountAmount;
    }
    
    public Money getExtendedDiscountAmountAsMoney()
    {
       return convertToMoney(getExtendedDiscountAmount());
    }

    public void setExtendedDiscountAmount(BigDecimal extendedDiscountAmount) {
        this.extendedDiscountAmount = extendedDiscountAmount;
    }

    public ActionCode getActionCode() {
        return actionCode;
    }

    public void setActionCode(ActionCode actionCode) {
        this.actionCode = actionCode;
    }

    public EntryMethod getEntryMethod() {
        return entryMethod;
    }

    public void setEntryMethod(EntryMethod entryMethod) {
        this.entryMethod = entryMethod;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public Boolean getGiftReceiptFlag() {
        return giftReceiptFlag;
    }

    public void setGiftReceiptFlag(Boolean giftReceiptFlag) {
        this.giftReceiptFlag = giftReceiptFlag;
    }

    public List<RetailPriceModifier> getModifiers() {
        return modifiers;
    }

    public void setModifiers(List<RetailPriceModifier> modifiers) {
        this.modifiers = modifiers;
    }

    public List<RestrictionValidationModifier> getRestrictionValidationModifiers() {
        return restrictionValidationModifiers;
    }

    public void setRestrictionValidationModifiers(
            List<RestrictionValidationModifier> restrictionValidationModifiers) {
        this.restrictionValidationModifiers = restrictionValidationModifiers;
    }

    public List<SaleReturnTaxLineItem> getSaleReturnTaxLineItems() {
        return saleReturnTaxLineItems;
    }

    public void setSaleReturnTaxLineItems(List<SaleReturnTaxLineItem> saleReturnTaxLineItems) {
        this.saleReturnTaxLineItems = saleReturnTaxLineItems;
    }

    protected Money convertToMoney(BigDecimal value)
    {
        return new Money(getRetailTransaction().getCurrency(),value);
    }
    
}
