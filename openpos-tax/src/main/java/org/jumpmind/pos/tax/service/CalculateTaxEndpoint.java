package org.jumpmind.pos.tax.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jumpmind.pos.service.Endpoint;
import org.jumpmind.pos.tax.model.AuthorityModel;
import org.jumpmind.pos.tax.model.GroupRuleModel;
import org.jumpmind.pos.tax.model.ItemTax;
import org.jumpmind.pos.tax.model.JurisdictionModel;
import org.jumpmind.pos.tax.model.RateRuleModel;
import org.jumpmind.pos.tax.model.TaxAmount;
import org.jumpmind.pos.tax.model.TaxConstants;
import org.jumpmind.pos.tax.model.TaxContainer;
import org.jumpmind.pos.tax.model.TaxRepository;
import org.jumpmind.pos.tax.model.TaxableItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(transactionManager="taxTxManager")
public class CalculateTaxEndpoint {

    @Autowired
    private TaxRepository taxRepository;

    /**
     * Re-calculate the tax for the RetailTransaction, re-creating the
     * SaleReturnTaxLineItems and TaxLineItems
     * 
     */
    @Endpoint("/calculateTax")
    public TaxCalculationResponse calculateTax(TaxCalculationRequest request) {
        Collection<AuthorityModel> authorities = getAuthorities(request.getGeoCode());
        TaxContainer container = new TaxContainer();

        for (TaxableItem item : request.getTaxableItems()) {
            for (AuthorityModel authority : authorities) {
                GroupRuleModel groupRule = authority.getGroupRule(item.getGroupId());
                if (groupRule != null) {
                	container.add(groupRule, item);
                } 
            }
        }

        boolean usesCompounding = container.usesCompounding();

        TaxCalculationResponse response = new TaxCalculationResponse();

        for (GroupRuleModel groupRule : container.getGroupRules()) {
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

        addTaxAmounts(request, response);
        return response;
    }

    public Collection<AuthorityModel> getAuthorities(String geoCode) {

        List<JurisdictionModel> result = taxRepository.findTaxJurisdictions(geoCode);
        List<AuthorityModel> authorities = new ArrayList<AuthorityModel>();
        for (JurisdictionModel rule : result) {
            AuthorityModel athy = rule.getAuthority();
            if (athy != null) {
                authorities.add(athy);
            }
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
    private void applyTaxAtTransaction(TaxCalculationResponse response, GroupRuleModel groupRule, Collection<TaxableItem> items,
            boolean usesCompounding) {
        BigDecimal taxableAmount = BigDecimal.ZERO;
        for (TaxableItem item : items) {
            taxableAmount = taxableAmount.add(getTaxableAmount(response, item, usesCompounding));
        }

        BigDecimal taxAmount = calculateTax(groupRule, taxableAmount);
        taxAmount = groupRule.getAuthority().round(taxAmount);
        prorate(response, groupRule, items, taxableAmount, taxAmount, usesCompounding);
    }

    /**
     * Calculate tax and round at the item level
     * 
     * @param groupRule
     * @param items
     */
    private void applyTaxAtItem(TaxCalculationResponse response, GroupRuleModel groupRule, Collection<TaxableItem> items,
            boolean usesCompounding) {
        for (TaxableItem item : items) {
            BigDecimal taxableAmount = getTaxableAmount(response, item, usesCompounding);
            BigDecimal taxAmount = calculateTax(groupRule, taxableAmount);
            taxAmount = groupRule.getAuthority().round(taxAmount);

            TaxAmount itemTaxAmount = new TaxAmount(groupRule.getAuthority().getId(), groupRule.getGroupId(), taxableAmount, taxAmount,
                    getTaxPercent(groupRule));

            response.addItemTaxAmount(item.getSequenceNumber(), itemTaxAmount);
        }
    }

    /**
     * Calculate tax at the item level and round at the transaction level, then
     * prorate the tax amount across the SaleReturnTaxLineItems
     * 
     * @param groupRule
     * @param items
     */
    private void applyTaxAtItemTransaction(TaxCalculationResponse response, GroupRuleModel groupRule, Collection<TaxableItem> items,
            boolean usesCompounding) {
        BigDecimal totalTaxableAmount = BigDecimal.ZERO;
        BigDecimal totalTaxAmount = BigDecimal.ZERO;
        for (TaxableItem item : items) {
            BigDecimal taxableAmount = getTaxableAmount(response, item, usesCompounding);
            BigDecimal taxAmount = calculateTax(groupRule, taxableAmount);
            totalTaxableAmount = totalTaxableAmount.add(taxableAmount);
            totalTaxAmount = totalTaxAmount.add(taxAmount);
        }
        totalTaxAmount = groupRule.getAuthority().round(totalTaxAmount);
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
    private void prorate(TaxCalculationResponse response, GroupRuleModel groupRule, Collection<TaxableItem> items, BigDecimal totalTaxableAmount,
            BigDecimal totalTaxAmount, boolean usesCompounding) {

        for (TaxableItem item : items) {
            BigDecimal taxableAmount = getTaxableAmount(response, item, usesCompounding);
            BigDecimal taxAmount = BigDecimal.ZERO;
            if (totalTaxableAmount.signum() != 0) {
                taxAmount = taxableAmount.multiply(totalTaxAmount).divide(totalTaxableAmount, BigDecimal.ROUND_DOWN);
                taxAmount = groupRule.getAuthority().round(taxAmount);
            }

            TaxAmount itemTaxAmount = new TaxAmount(groupRule.getAuthority().getId(), groupRule.getGroup().getId(), taxableAmount,
                    taxAmount, getTaxPercent(groupRule));

            response.addItemTaxAmount(item.getSequenceNumber(), itemTaxAmount);

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
    private void addTaxAmounts(TaxCalculationRequest request, TaxCalculationResponse response) {
        for (ItemTax itemTax : response.getItemTaxes()) {

            for (TaxAmount itemTaxAmount : itemTax.getItemTaxAmounts()) {
                TaxAmount taxAmount = response.getTaxAmount(itemTaxAmount.getAuthorityId(), itemTaxAmount.getGroupId());
                if (taxAmount == null) {
                    taxAmount = new TaxAmount(itemTaxAmount);
                    response.addTaxAmount(taxAmount);
                } else {
                    taxAmount.add(itemTaxAmount);
                }
            }
        }
    }

    private BigDecimal getTaxableAmount(TaxCalculationResponse response, TaxableItem item, boolean usesCompounding) {
        BigDecimal taxableAmount = item.getExtendedAmount();

        ItemTax itemTax = response.getItemTax(item.getSequenceNumber());
        if (usesCompounding && itemTax != null) {
            for (TaxAmount taxAmount : itemTax.getItemTaxAmounts()) {
                taxableAmount = taxableAmount.add(taxAmount.getTaxAmount());
            }
        }

        return taxableAmount;
    }

    private BigDecimal getTaxPercent(GroupRuleModel groupRule) {
        for (RateRuleModel rateRule : groupRule.getRateRules()) {
            if (rateRule.getTypeCode() == RateRuleModel.TYPE_PERCENT_RATE) {
                return rateRule.getTaxPercent();
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
    private BigDecimal calculateTax(GroupRuleModel groupRule, BigDecimal amount) {
        String usageCode = groupRule.getRateRuleUsageCode();
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
    private BigDecimal calculatePickOne(GroupRuleModel groupRule, BigDecimal amount) {
        BigDecimal tax = BigDecimal.ZERO;
        for (RateRuleModel rateRule : groupRule.getRateRules()) {
            if (rateRule.getTypeCode() == RateRuleModel.TYPE_FLAT_RATE) {
                tax = tax.add(rateRule.getTaxAmount());
            } else if (rateRule.getTypeCode() == RateRuleModel.TYPE_PERCENT_RATE) {
                tax = tax.add(rateRule.getTaxPercent().movePointLeft(2).multiply(amount));
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
    private BigDecimal calculateTaxTable(GroupRuleModel groupRule, BigDecimal amount) {
        Collection<RateRuleModel> rateRules = groupRule.getRateRules();
        RateRuleModel firstBreak = (RateRuleModel) groupRule.getFirstRateRule();
        RateRuleModel lastBreak = (RateRuleModel) groupRule.getLastRateRule();

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
            BigDecimal cycleTax = lastBreak.getTaxAmount().subtract(nonCycleTax);

            BigDecimal workingAmount = amount.subtract(nonCycleAmount);
            BigDecimal times = new BigDecimal(workingAmount.divide(cycleAmount, BigDecimal.ROUND_DOWN).toBigInteger());
            BigDecimal calculatedTax = cycleTax.multiply(times);
            workingAmount = amount.subtract(cycleAmount.multiply(times));
            BigDecimal remainingTax = lookupRateRuleTax(rateRules, workingAmount);
            tax = tax.add(calculatedTax).add(remainingTax);
        }
        return tax;
    }

    private BigDecimal lookupRateRuleTax(Collection<RateRuleModel> rateRules, BigDecimal taxableAmount) {
        RateRuleModel rateRule = lookupRateRule(rateRules, taxableAmount);
        if (rateRule != null) {
            return rateRule.getTaxAmount();
        }
        return BigDecimal.ZERO;
    }

    private RateRuleModel lookupRateRule(Collection<RateRuleModel> rateRules, BigDecimal taxableAmount) {
        for (RateRuleModel rateRule : rateRules) {
            if (taxableAmount.compareTo(rateRule.getMaxTaxableAmount()) <= 0) {
                return rateRule;
            }
        }
        return null;
    }

}
