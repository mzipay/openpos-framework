package org.jumpmind.pos.tax.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A type of Transaction that records the business conducted between a
 * BusinessUnit and another party involving the exchange in ownership and/or
 * accountability for merchandise and/or tender or involving the exchange of
 * tender for services.
 */
public class RetailTransaction {

    private String id;

    private int sequenceNumber;

    private String businessUnitId;

    private BigDecimal subTotalAmount = BigDecimal.ZERO;

    private BigDecimal totalAmount = BigDecimal.ZERO;

    private BigDecimal totalDiscountAmount = BigDecimal.ZERO;

    private BigDecimal totalTaxAmount = BigDecimal.ZERO;

    private BigDecimal totalQuantity = BigDecimal.ZERO;

    private RetailTransactionType retailTransactionType;

    private List<RetailTransactionLineItem> lineItems;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setBusinessUnitId(String retailStoreId) {
        this.businessUnitId = retailStoreId;
    }

    public String getBusinessUnitId() {
        return businessUnitId;
    }

    public List<TaxLineItem> getTaxLineItems() {
        return new TypeLineItemFilter<TaxLineItem>().filter(lineItems, TaxLineItem.class);
    }

    public void removeTaxLineItems() {
        lineItems.removeAll(getTaxLineItems());
    }

    public TaxLineItem getTaxLineItem(TaxAuthority taxAuthority, TaxableGroup taxableGroup) {
        for (TaxLineItem taxLineItem : getTaxLineItems()) {
            if (taxLineItem.getTaxAuthority().equals(taxAuthority) && taxLineItem.getTaxableGroup().equals(taxableGroup)) {
                return taxLineItem;
            }
        }
        return null;
    }

    public List<SaleReturnLineItem> getSaleReturnLineItems() {
        return new TypeLineItemFilter<SaleReturnLineItem>().filter(lineItems, SaleReturnLineItem.class);
    }

    public List<SaleReturnLineItem> getSaleReturnLineItems(final ActionCode actionCode, final boolean isVoid) {
        return new TypeLineItemFilter<SaleReturnLineItem>() {
            @Override
            public boolean shouldInclude(final SaleReturnLineItem lineItem) {
                return lineItem.isVoidFlag() == isVoid && lineItem.getActionCode() == actionCode;
            }
        }.filter(lineItems, SaleReturnLineItem.class);
    }

    public List<SaleReturnTaxLineItem> getSaleReturnTaxLineItems() {
        List<SaleReturnTaxLineItem> list = new ArrayList<SaleReturnTaxLineItem>();
        for (SaleReturnLineItem lineItem : getSaleReturnLineItems()) {
            list.addAll(lineItem.getSaleReturnTaxLineItems());
        }
        return list;
    }

    public void setRetailTransactionType(RetailTransactionType retailTransactionType) {
        this.retailTransactionType = retailTransactionType;
    }

    public RetailTransactionType getRetailTransactionType() {
        return retailTransactionType;
    }

    public List<RetailTransactionLineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<RetailTransactionLineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public void addLineItem(RetailTransactionLineItem lineItem) {
        if (lineItems == null) {
            lineItems = new ArrayList<RetailTransactionLineItem>();
        }
        lineItems.add(lineItem);
    }

    public void addLineItems(Collection<RetailTransactionLineItem> lineItems) {
        for (RetailTransactionLineItem lineItem : lineItems) {
            addLineItem(lineItem);
        }
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public BigDecimal getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalTaxAmount(BigDecimal totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public BigDecimal getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setSubTotalAmount(BigDecimal subTotalAmount) {
        this.subTotalAmount = subTotalAmount;
    }

    public BigDecimal getSubTotalAmount() {
        return subTotalAmount;
    }

    public void setTotalQuantity(BigDecimal totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalQuantity() {
        return totalQuantity;
    }

}