package org.jumpmind.pos;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;

import org.jumpmind.pos.tax.model.CalculateTaxRateRule;
import org.jumpmind.pos.tax.model.FlatTaxRateRule;
import org.jumpmind.pos.tax.model.TaxAuthority;
import org.jumpmind.pos.tax.model.TaxConstants;
import org.jumpmind.pos.tax.model.TaxGroupRule;
import org.jumpmind.pos.tax.model.TaxableGroup;
import org.jumpmind.pos.tax.service.CalculateTaxEndpoint;

public class MockCalculateTaxEndpoint extends CalculateTaxEndpoint {

    @Override
    public Collection<TaxAuthority> getTaxAuthorities(String storeId) {
        Collection<TaxAuthority> authorities = new ArrayList<TaxAuthority>();

        authorities.add(getFirstAuthority());
        authorities.add(getSecondAuthority());
        authorities.add(getThirdAuthority());

        return authorities;
    }

    private TaxAuthority getFirstAuthority() {
        TaxAuthority authority = new TaxAuthority();
        authority.setId("1");
        authority.setRoundingCode(TaxConstants.ROUNDING_HALF_UP);
        authority.setRoundingDigitsQuantity(new Integer(2));

        addPercentRule(authority, "100", 5.25, TaxConstants.CALCULATION_TRANSACTION);
        addPercentRule(authority, "101", 5.25, TaxConstants.CALCULATION_TRANSACTION);
        addPercentRule(authority, "102", 5.25, TaxConstants.CALCULATION_TRANSACTION);
        addPercentRule(authority, "200", 5.25, TaxConstants.CALCULATION_ITEM);
        addPercentRule(authority, "300", 5.25, TaxConstants.CALCULATION_ITEM_TRANSACTION);
        addFlatRule(authority, "400", 0.05, TaxConstants.CALCULATION_ITEM);
        addFlatRule(authority, "401", 0.10, TaxConstants.CALCULATION_ITEM);

        addPercentRule(authority, "450", 1.25, TaxConstants.CALCULATION_TRANSACTION);
        authority.getTaxGroupRule("450").setCompoundSequenceNumber(1);

        // Florida 6.0%
        addTableRule(authority, "500", new double[] { 0, 0.10, 0.17, 0.34, 0.51, 0.67, 0.84, 1.10 }, 1.00,
                TaxConstants.CALCULATION_TRANSACTION);
        addTableRule(authority, "501", new double[] { 0, 0.10, 0.17, 0.34, 0.51, 0.67, 0.84, 1.10 }, 1.00,
                TaxConstants.CALCULATION_TRANSACTION);
        // Idaho 6.0%
        addTableRule(authority, "600", new double[] { 0, 0.12, 0.21, 0.38, 0.54, 0.71, 0.88 }, 0.50, TaxConstants.CALCULATION_TRANSACTION);

        return authority;
    }

    private TaxAuthority getSecondAuthority() {
        TaxAuthority authority = new TaxAuthority();
        authority.setId("2");
        authority.setRoundingCode(TaxConstants.ROUNDING_HALF_DOWN);
        authority.setRoundingDigitsQuantity(new Integer(2));

        addPercentRule(authority, "700", 7.75, TaxConstants.CALCULATION_TRANSACTION);

        return authority;
    }

    private TaxAuthority getThirdAuthority() {
        TaxAuthority authority = new TaxAuthority();
        authority.setId("3");
        authority.setRoundingCode(TaxConstants.ROUNDING_HALF_UP);
        authority.setRoundingDigitsQuantity(new Integer(2));

        addPercentRule(authority, "450", 5.25, TaxConstants.CALCULATION_TRANSACTION);
        authority.getTaxGroupRule("450").setCompoundSequenceNumber(2);

        return authority;
    }

    private TaxGroupRule addGroup(TaxAuthority authority, TaxableGroup taxGroup, String method) {
        TaxGroupRule groupRule = new TaxGroupRule();
        groupRule.setTaxAuthority(authority);
        groupRule.setTaxableGroup(taxGroup);
        groupRule.setCalculationMethodCode(method);
        groupRule.setTaxRateRuleUsageCode(TaxConstants.USAGE_PICK_ONE);
        authority.addTaxGroupRule(groupRule);
        return groupRule;
    }

    private CalculateTaxRateRule addPercentRule(TaxAuthority authority, String taxGroupId, double percent, String method) {
        TaxableGroup taxGroup = new TaxableGroup(taxGroupId);
        TaxGroupRule groupRule = addGroup(authority, taxGroup, method);

        CalculateTaxRateRule rateRule = new CalculateTaxRateRule();
        rateRule.setPercent(new BigDecimal(percent));
        groupRule.addTaxRateRule(rateRule);
        return rateRule;
    }

    private FlatTaxRateRule addFlatRule(TaxAuthority authority, String taxGroupId, double amount, String method) {
        TaxableGroup taxGroup = new TaxableGroup(taxGroupId);
        TaxGroupRule groupRule = addGroup(authority, taxGroup, method);

        FlatTaxRateRule rateRule = new FlatTaxRateRule();
        rateRule.setAmount(new BigDecimal(amount));
        groupRule.addTaxRateRule(rateRule);
        return rateRule;
    }

    private void addTableRule(TaxAuthority authority, String taxGroupId, double[] breaks, double cycleAmount, String method) {
        TaxableGroup taxGroup = new TaxableGroup(taxGroupId);
        TaxGroupRule groupRule = addGroup(authority, taxGroup, method);
        groupRule.setTaxRateRuleUsageCode(TaxConstants.USAGE_TAX_TABLE);
        groupRule.setCycleAmount(new BigDecimal(cycleAmount));

        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal penny = new BigDecimal("0.01");
        MathContext mc = new MathContext(2, RoundingMode.HALF_UP);
        for (int i = 0; i < breaks.length - 1; i++) {
            BigDecimal minTaxableAmount = new BigDecimal(breaks[i], mc);
            BigDecimal maxTaxableAmount = new BigDecimal(breaks[i + 1], mc).subtract(penny);
            FlatTaxRateRule rateRule = new FlatTaxRateRule();
            rateRule.setMinTaxableAmount(minTaxableAmount);
            rateRule.setMaxTaxableAmount(maxTaxableAmount);
            rateRule.setAmount(amount);
            groupRule.addTaxRateRule(rateRule);
            amount = amount.add(penny);
        }
    }

}
