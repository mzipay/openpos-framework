package org.jumpmind.pos.tax.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * A line item component of a RETAIL TRANSACTION that records the exchange in
 * ownership of a merchandise item (i.e. a sale or return) or the sale or refund
 * related to a service.
 */
public class SaleReturnLineItem extends RetailTransactionLineItem {

    private String posItemId;

    private String itemId;

    private String taxGroupId;

    private ItemType itemType;

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
    private BigDecimal extendedAmount = BigDecimal.ZERO;

    private ActionCode actionCode = ActionCode.SALE_ITEM;

    private String reasonCode;

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

    public void setExtendedAmount(BigDecimal extendedAmount) {
        this.extendedAmount = extendedAmount;
    }

    public ActionCode getActionCode() {
        return actionCode;
    }

    public void setActionCode(ActionCode actionCode) {
        this.actionCode = actionCode;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public List<SaleReturnTaxLineItem> getSaleReturnTaxLineItems() {
        return saleReturnTaxLineItems;
    }

    public void setSaleReturnTaxLineItems(List<SaleReturnTaxLineItem> saleReturnTaxLineItems) {
        this.saleReturnTaxLineItems = saleReturnTaxLineItems;
    }

}
