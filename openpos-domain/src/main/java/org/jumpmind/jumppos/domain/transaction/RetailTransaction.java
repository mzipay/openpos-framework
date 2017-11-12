package org.jumpmind.jumppos.domain.transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.jumpmind.jumppos.domain.business.Customer;
import org.jumpmind.jumppos.domain.currency.Currency;
import org.jumpmind.jumppos.domain.item.SalesRestriction;
import org.jumpmind.jumppos.domain.tax.TaxAuthority;
import org.jumpmind.jumppos.domain.tax.TaxableGroup;
import org.jumpmind.jumppos.domain.util.AbstractLineItemFilter;
import org.jumpmind.jumppos.domain.util.TypeLineItemFilter;

/**
 * A type of Transaction that records the business conducted between a
 * BusinessUnit and another party involving the exchange in ownership and/or
 * accountability for merchandise and/or tender or involving the exchange of
 * tender for services.
 */
@Entity
public class RetailTransaction extends Transaction {

    @OneToOne
    private Customer customer;

    private int ringElapsedTime;

    private int tenderElapsedTime;

    private int idleElapsedTime;

    private int lockElapsedTime;

    private BigDecimal subTotalAmount = BigDecimal.ZERO;

    private BigDecimal totalAmount = BigDecimal.ZERO;

    private BigDecimal totalDiscountAmount = BigDecimal.ZERO;

    private BigDecimal totalTaxAmount = BigDecimal.ZERO;
    
    private BigDecimal totalQuantity = BigDecimal.ZERO;

    @OneToOne
    private Currency currency;

    private RetailTransactionType retailTransactionType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "retailTransaction")
    private List<RetailTransactionLineItem> lineItems;

    public List<TaxLineItem> getTaxLineItems() {
        return new TypeLineItemFilter<TaxLineItem>().filter(lineItems, TaxLineItem.class);
    }

    public void removeTaxLineItems() {
        lineItems.removeAll(getTaxLineItems());
    }

    public TaxLineItem getTaxLineItem(TaxAuthority taxAuthority, TaxableGroup taxableGroup) {
        for (TaxLineItem taxLineItem : getTaxLineItems()) {
            if (taxLineItem.getTaxAuthority().equals(taxAuthority)
                    && taxLineItem.getTaxableGroup().equals(taxableGroup)) {
                return taxLineItem;
            }
        }
        return null;
    }

    public List<SaleReturnLineItem> getSaleReturnLineItems() {
        return new TypeLineItemFilter<SaleReturnLineItem>().filter(lineItems,
                SaleReturnLineItem.class);
    }

    public List<SaleReturnLineItem> getSaleReturnLineItems(final ActionCode actionCode,
            final boolean isVoid) {
        return new AbstractLineItemFilter<SaleReturnLineItem>() {
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getIdleElapsedTime() {
        return idleElapsedTime;
    }

    public void setIdleElapsedTime(int idleElapsedTime) {
        this.idleElapsedTime = idleElapsedTime;
    }

    public int getLockElapsedTime() {
        return lockElapsedTime;
    }

    public void setLockElapsedTime(int lockElapsedTime) {
        this.lockElapsedTime = lockElapsedTime;
    }

    public int getRingElapsedTime() {
        return ringElapsedTime;
    }

    public void setRingElapsedTime(int ringElapsedTime) {
        this.ringElapsedTime = ringElapsedTime;
    }

    public int getTenderElapsedTime() {
        return tenderElapsedTime;
    }

    public void setTenderElapsedTime(int tenderElapsedTime) {
        this.tenderElapsedTime = tenderElapsedTime;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
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

    /**
     * Check to see if a specific restriction validation question has already
     * been asked during the lifetime of this transaction.
     */
    public boolean isSalesRestrictionAlreadyMet(SalesRestriction restriction) {
        // for (SaleReturnLineItem item : getSaleReturnLineItems()) {
        // if (item.getRestrictionValidationModifiers() != null) {
        // Collection<RestrictionValidationModifier> mods = item
        // .getRestrictionValidationModifiers();
        // for (RestrictionValidationModifier modifier : mods) {
        // if (modifier.getRestrictionValidationQuestion().getId()
        // .equals(
        // restriction
        // .getRestrictionValidationQuestion()
        // .getId())) {
        // return true;
        // }
        // }
        // }
        // }
        return false;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
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

    public void recalculateTotals() {
        totalAmount = BigDecimal.ZERO;
        subTotalAmount = BigDecimal.ZERO;
        totalDiscountAmount = BigDecimal.ZERO;
        totalTaxAmount = BigDecimal.ZERO;
        totalQuantity = BigDecimal.ZERO;
        List<SaleReturnLineItem> lineItems = getSaleReturnLineItems();
        if (lineItems != null) {
            for (SaleReturnLineItem saleReturnLineItem : lineItems) {
                subTotalAmount = subTotalAmount.add(saleReturnLineItem.getExtendedAmount());    
                totalQuantity = totalQuantity.add(saleReturnLineItem.getQuantitySold());
                
                List<SaleReturnTaxLineItem> taxableAmounts = saleReturnLineItem.getSaleReturnTaxLineItems();
                if (taxableAmounts != null) {
                    for (SaleReturnTaxLineItem saleReturnTaxLineItem : taxableAmounts) {
                        totalTaxAmount = totalTaxAmount.add(saleReturnTaxLineItem.getTaxAmount());
                    }
                }
            }
            
            totalAmount = subTotalAmount;
        }
        
        // TODO discounts
        
    }
}