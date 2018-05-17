package org.jumpmind.pos.tax.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jumpmind.pos.service.Endpoint;
import org.jumpmind.pos.tax.model.ActionCode;
import org.jumpmind.pos.tax.model.CalculateTaxRateRule;
import org.jumpmind.pos.tax.model.FlatTaxRateRule;
import org.jumpmind.pos.tax.model.ItemTaxAmount;
import org.jumpmind.pos.tax.model.TaxAuthority;
import org.jumpmind.pos.tax.model.TaxAuthorityRule;
import org.jumpmind.pos.tax.model.TaxCalculationRequest;
import org.jumpmind.pos.tax.model.TaxCalculationResponse;
import org.jumpmind.pos.tax.model.TaxConstants;
import org.jumpmind.pos.tax.model.TaxContainer;
import org.jumpmind.pos.tax.model.TaxGroupRule;
import org.jumpmind.pos.tax.model.TaxRateRule;
import org.jumpmind.pos.tax.model.TaxRepository;
import org.jumpmind.pos.tax.model.TaxableItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class CalculateTaxEndpoint {

    @Autowired
    TaxRepository taxRepository;

    /**
     * Re-calculate the tax for the RetailTransaction, re-creating the
     * SaleReturnTaxLineItems and TaxLineItems
     * 
     */
    @Endpoint("/calculateTax")
    public TaxCalculationResponse calculateTax(TaxCalculationRequest request) {
        Collection<TaxAuthority> authorities = getTaxAuthorities(request.getTaxCalculationGeocode());
        TaxContainer container = new TaxContainer();

        for (TaxableItem item : request.getTaxableItems()) {
            for (TaxAuthority authority : authorities) {
                TaxGroupRule groupRule = authority.getTaxGroupRule(item.getTaxGroupId());
                container.add(groupRule, item);
            }
        }

        boolean usesCompounding = container.usesCompounding();

        TaxCalculationResponse response = new TaxCalculationResponse();

        for (TaxGroupRule groupRule : container.getTaxGroupRules()) {
            Collection<TaxableItem> lineItems = container.getItems(groupRule);
            String method = groupRule.getCalculationMethodCode();
            if (method == null) {
                method = TaxConstants.CALCULATION_TRANSACTION;
            }
            if (method.equals(TaxConstants.CALCULATION_ITEM)) {
                applyTaxAtItem(response, groupRule, lineItems, usesCompounding);
            } else if (method.equals(TaxConstants.CALCULATION_ITEM_TRANSACTION)) {
                applyTaxAtItemTransaction(response, groupRule, lineItems, usesCompounding);
            } else {
                applyTaxAtTransaction(response, groupRule, lineItems, usesCompounding);
            }
        }

        addTaxLineItems(request, response);
        return response;
    }

    public Collection<TaxAuthority> getTaxAuthorities(String taxCalculationGeocode) {

        List<TaxAuthorityRule> result = taxRepository.findTaxAuthorityRules(taxCalculationGeocode);
        List<TaxAuthority> authorities = new ArrayList<TaxAuthority>();
        for (TaxAuthorityRule rule : result) {
            TaxAuthority athy = rule.getTaxAuthority();
            if (athy != null) {
                authorities.add(athy);
            }
        }

        if (authorities.size() == 0) {
            // logger.warn("Could not find a tax authority for " +
            // businessUnitId);
        }
        return authorities;

    }

    /**
     * Calculate tax and round at the transaction level, then prorate the tax
     * amount across the SaleReturnTaxLineItems
     * 
     * @param groupRule
     * @param items
     */
    private void applyTaxAtTransaction(TaxCalculationResponse response, TaxGroupRule groupRule, Collection<TaxableItem> items,
            boolean usesCompounding) {
        BigDecimal taxableAmount = BigDecimal.ZERO;
        for (TaxableItem item : items) {
            taxableAmount = taxableAmount.add(getTaxableAmount(item, usesCompounding));
        }

        BigDecimal taxAmount = calculateTax(groupRule, taxableAmount);
        taxAmount = groupRule.getTaxAuthority().round(taxAmount);
        prorate(response, groupRule, items, taxableAmount, taxAmount, usesCompounding);
    }

    /**
     * Calculate tax and round at the item level
     * 
     * @param groupRule
     * @param items
     */
    private void applyTaxAtItem(TaxCalculationResponse response, TaxGroupRule groupRule, Collection<TaxableItem> items,
            boolean usesCompounding) {
        for (TaxableItem item : items) {
            BigDecimal taxableAmount = getTaxableAmount(item, usesCompounding);
            BigDecimal taxAmount = calculateTax(groupRule, taxableAmount);
            taxAmount = groupRule.getTaxAuthority().round(taxAmount);

            ItemTaxAmount itemTaxAmount = new ItemTaxAmount(item.getSequenceNumber(), groupRule.getTaxAuthority().getId(),
                    groupRule.getTaxableGroup().getId(), taxableAmount, taxAmount, getTaxPercent(groupRule));

            response.addItemTaxAmount(itemTaxAmount);
            // item.addSaleReturnTaxLineItem(new
            // SaleReturnTaxLineItem(groupRule.getTaxAuthority(),
            // groupRule.getTaxableGroup(),
            // item.getExtendedAmount(), taxAmount, getTaxPercent(groupRule)));
        }
    }

    /**
     * Calculate tax at the item level and round at the transaction level, then
     * prorate the tax amount across the SaleReturnTaxLineItems
     * 
     * @param groupRule
     * @param items
     */
    private void applyTaxAtItemTransaction(TaxCalculationResponse response, TaxGroupRule groupRule, Collection<TaxableItem> items,
            boolean usesCompounding) {
        BigDecimal totalTaxableAmount = BigDecimal.ZERO;
        BigDecimal totalTaxAmount = BigDecimal.ZERO;
        for (TaxableItem item : items) {
            BigDecimal taxableAmount = getTaxableAmount(item, usesCompounding);
            BigDecimal taxAmount = calculateTax(groupRule, taxableAmount);
            totalTaxableAmount = totalTaxableAmount.add(taxableAmount);
            totalTaxAmount = totalTaxAmount.add(taxAmount);
        }
        totalTaxAmount = groupRule.getTaxAuthority().round(totalTaxAmount);
        prorate(response, groupRule, items, totalTaxableAmount, totalTaxAmount, usesCompounding);
    }

    /**
     * Prorate the given tax amount across all SaleReturnLineItems
     * 
     * @param groupRule
     * @param items
     * @param taxableAmount
     * @param taxAmount
     */
    private void prorate(TaxCalculationResponse response, TaxGroupRule groupRule, Collection<TaxableItem> items,
            BigDecimal totalTaxableAmount, BigDecimal totalTaxAmount, boolean usesCompounding) {
        for (TaxableItem item : items) {
            BigDecimal taxableAmount = getTaxableAmount(item, usesCompounding);
            BigDecimal taxAmount = BigDecimal.ZERO;
            if (totalTaxableAmount.signum() != 0) {
                taxAmount = taxableAmount.multiply(totalTaxAmount).divide(totalTaxableAmount, BigDecimal.ROUND_DOWN);
                taxAmount = groupRule.getTaxAuthority().round(taxAmount);
            }
            // item.addSaleReturnTaxLineItem(new SaleReturnTaxLineItem(groupRule.getTaxAuthority(), groupRule.getTaxableGroup(), taxableAmount,
            //        taxAmount, getTaxPercent(groupRule)));
            totalTaxAmount = totalTaxAmount.subtract(taxAmount);
            totalTaxableAmount = totalTaxableAmount.subtract(taxableAmount);
        }
    }

    /**
     * Add up all the SaleReturnTaxLineItems to create TaxLineItems that are
     * added as lines on the RetailTransaction
     * 
     * @param request
     * @param lineItems
     */
    private void addTaxLineItems(TaxCalculationRequest request, TaxCalculationResponse response) {
        for (TaxableItem lineItem : request.getTaxableItems()) {
        	/*
            for (SaleReturnTaxLineItem saleReturnTaxLineItem : lineItem.getSaleReturnTaxLineItems()) {
                TaxLineItem taxLineItem = request.getTaxLineItem(saleReturnTaxLineItem.getTaxAuthority(),
                        saleReturnTaxLineItem.getTaxableGroup());
                if (taxLineItem == null) {
                    taxLineItem = new TaxLineItem(saleReturnTaxLineItem);
                    response.addLineItem(taxLineItem);
                } else {
                    taxLineItem.add(saleReturnTaxLineItem);
                }
            }
            */
        }
    }

    private BigDecimal getTaxableAmount(TaxableItem item, boolean usesCompounding) {
        BigDecimal taxableAmount = item.getExtendedAmount();
        /*
        if (usesCompounding && item.getSaleReturnTaxLineItems() != null) {
            for (SaleReturnTaxLineItem taxLineItem : item.getSaleReturnTaxLineItems()) {
                taxableAmount = taxableAmount.add(taxLineItem.getTaxAmount());
            }
        }
        */
        return taxableAmount;
    }

    private BigDecimal getTaxPercent(TaxGroupRule groupRule) {
        for (TaxRateRule rateRule : groupRule.getTaxRateRules()) {
            if (rateRule instanceof CalculateTaxRateRule) {
                CalculateTaxRateRule calcRateRule = (CalculateTaxRateRule) rateRule;
                return calcRateRule.getPercent();
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * Calculate tax amount given the group rule and taxable amount, using
     * either pickone (a percent or flat amount) or table
     * 
     * @param groupRule
     * @param amount
     * @return
     */
    private BigDecimal calculateTax(TaxGroupRule groupRule, BigDecimal amount) {
        String usageCode = groupRule.getTaxRateRuleUsageCode();
        if (usageCode != null && usageCode.equals(TaxConstants.USAGE_TAX_TABLE)) {
            return calculateTaxTable(groupRule, amount);
        } else {
            return calculatePickOne(groupRule, amount);
        }
    }

    /**
     * Calculate tax amount given the group rule and taxable amount, using
     * either percent or flat tax
     * 
     * @param groupRule
     * @param amount
     * @return
     */
    private BigDecimal calculatePickOne(TaxGroupRule groupRule, BigDecimal amount) {
        BigDecimal tax = BigDecimal.ZERO;
        for (TaxRateRule rateRule : groupRule.getTaxRateRules()) {
            if (rateRule instanceof FlatTaxRateRule) {
                FlatTaxRateRule flatRateRule = (FlatTaxRateRule) rateRule;
                tax = tax.add(flatRateRule.getAmount());
            } else if (rateRule instanceof CalculateTaxRateRule) {
                CalculateTaxRateRule calcRateRule = (CalculateTaxRateRule) rateRule;
                tax = tax.add(calcRateRule.getPercent().movePointLeft(2).multiply(amount));
            }
        }
        return tax;
    }

    /**
     * Calculate tax amount given the group rule and taxable amount, using a tax
     * table
     * 
     * @param groupRule
     * @param amount
     * @return
     */
    private BigDecimal calculateTaxTable(TaxGroupRule groupRule, BigDecimal amount) {
        Collection<TaxRateRule> rateRules = groupRule.getTaxRateRules();
        FlatTaxRateRule firstBreak = (FlatTaxRateRule) groupRule.getFirstTaxRateRule();
        FlatTaxRateRule lastBreak = (FlatTaxRateRule) groupRule.getLastTaxRateRule();

        BigDecimal tax = BigDecimal.ZERO;
        // If the taxable amount fits in the table, we just lookup the tax
        if (lastBreak.getMaxTaxableAmount().compareTo(amount) >= 0) {
            tax = lookupRateRuleTax(rateRules, amount);
        }
        // We calculate the number of times the table repeats for this taxable
        // amount
        else {
            BigDecimal cycleAmount = groupRule.getCycleAmount();
            if (cycleAmount == null || cycleAmount.signum() <= 0) {
                cycleAmount = lastBreak.getMaxTaxableAmount().subtract(firstBreak.getMaxTaxableAmount());
            }
            BigDecimal nonCycleAmount = lastBreak.getMaxTaxableAmount().subtract(cycleAmount);
            BigDecimal nonCycleTax = lookupRateRuleTax(rateRules, nonCycleAmount);
            BigDecimal cycleTax = lastBreak.getAmount().subtract(nonCycleTax);

            BigDecimal workingAmount = amount.subtract(nonCycleAmount);
            BigDecimal times = new BigDecimal(workingAmount.divide(cycleAmount, BigDecimal.ROUND_DOWN).toBigInteger());
            BigDecimal calculatedTax = cycleTax.multiply(times);
            workingAmount = amount.subtract(cycleAmount.multiply(times));
            BigDecimal remainingTax = lookupRateRuleTax(rateRules, workingAmount);
            tax = tax.add(calculatedTax).add(remainingTax);
        }
        return tax;
    }

    private BigDecimal lookupRateRuleTax(Collection<TaxRateRule> rateRules, BigDecimal taxableAmount) {
        FlatTaxRateRule rateRule = lookupRateRule(rateRules, taxableAmount);
        if (rateRule != null) {
            return rateRule.getAmount();
        }
        return BigDecimal.ZERO;
    }

    private FlatTaxRateRule lookupRateRule(Collection<TaxRateRule> rateRules, BigDecimal taxableAmount) {
        for (TaxRateRule rateRule : rateRules) {
            if (taxableAmount.compareTo(rateRule.getMaxTaxableAmount()) <= 0) {
                return (FlatTaxRateRule) rateRule;
            }
        }
        return null;
    }

}
