package org.jumpmind.pos;

import java.math.MathContext;
import java.math.RoundingMode;

import org.jumpmind.pos.tax.service.CalculateTaxEndpoint;
import org.junit.Before;
import org.junit.Ignore;

@Ignore
public class TaxServiceTest {
    CalculateTaxEndpoint calculateTaxEndpoint;

    MathContext mc = new MathContext(10, RoundingMode.HALF_UP);

    @Before
    public void setup() {
        calculateTaxEndpoint = new MockCalculateTaxEndpoint();
    }

//    @Test
//    public void testGroupPercentTax() {
//        RetailTransaction tran = getTransaction();
//        tran.addLineItem(getSaleLineItem("100", 0.99));
//        tran.addLineItem(getSaleLineItem("100", 1.99));
//        tran.addLineItem(getSaleLineItem("101", 6.99));
//        tran.addLineItem(getSaleLineItem("101", 3.99));
//        tran.addLineItem(getSaleLineItem("102", 4.99));
//        tran.addLineItem(getSaleLineItem("102", 5.99));
//        // calculateTaxEndpoint.calculateTax(tran);
//        assertTax(tran, "1", "100", 0.16, 5.25);
//        assertTax(tran, "1", "101", 0.58, 5.25);
//        assertTax(tran, "1", "101", 0.58, 5.25);
//    }
//
//    @Test
//    public void testTransactionTableTax() {
//        RetailTransaction tran = getTransaction();
//        tran.addLineItem(getSaleLineItem("500", 4.99));
//        tran.addLineItem(getSaleLineItem("500", 2.99));
//        // calculateTaxEndpoint.calculateTax(tran);
//        assertTax(tran, "1", "500", 0.48);
//    }
//
//    @Test
//    public void testTransactionTableCycleTax() {
//        RetailTransaction tran = getTransaction();
//        tran.addLineItem(getSaleLineItem("600", 1.71));
//        // calculateTaxEndpoint.calculateTax(tran);
//        assertTax(tran, "1", "600", 0.11);
//
//        tran = getTransaction();
//        tran.addLineItem(getSaleLineItem("600", 85.95));
//        // calculateTaxEndpoint.calculateTax(tran);
//        assertTax(tran, "1", "600", 5.16);
//    }
//
//    @Test
//    public void testCompoundingTax() {
//        RetailTransaction tran = getTransaction();
//        tran.addLineItem(getSaleLineItem("450", 1.04));
//        // calculateTaxEndpoint.calculateTax(tran);
//        assertTax(tran, "1", "450", 0.01, 1.25);
//        assertTax(tran, "3", "450", 0.06, 5.25);
//    }
//
//    private RetailTransaction getTransaction() {
//        RetailTransaction tran = new RetailTransaction();
//        tran.setBusinessUnitId("00001");
//        return tran;
//    }
//
//    private RetailTransactionLineItem getSaleLineItem(String taxGroup, double amount) {
//        return getSaleReturnLineItem(taxGroup, amount, ActionCode.SALE_ITEM);
//    }
//
//    private RetailTransactionLineItem getSaleReturnLineItem(String taxGroup, double amount, ActionCode actionCode) {
//        SaleReturnLineItem lineItem = new SaleReturnLineItem();
//        lineItem.setActionCode(actionCode);
//        lineItem.setTaxGroupId(taxGroup);
//        lineItem.setExtendedAmount(new BigDecimal(amount));
//        return lineItem;
//    }
//
//    private void assertTax(RetailTransaction tran, String authorityId, String groupId, double expectedAmount) {
//        TaxLineItem taxLineItem = tran.getTaxLineItem(new TaxAuthority(authorityId), new TaxableGroup(groupId));
//        assertNotNull("Expected tax for tax group " + authorityId + "-" + groupId, taxLineItem);
//        assertTrue("Expected tax of " + expectedAmount + " instead of " + taxLineItem.getTaxAmount(),
//                taxLineItem.getTaxAmount().compareTo(new BigDecimal(expectedAmount, mc)) == 0);
//    }
//
//    private void assertTax(RetailTransaction tran, String authorityId, String groupId, double expectedAmount, double expectedPercent) {
//        assertTax(tran, authorityId, groupId, expectedAmount);
//        TaxLineItem taxLineItem = tran.getTaxLineItem(new TaxAuthority(authorityId), new TaxableGroup(groupId));
//        assertTrue("Expected percent of " + expectedPercent + " instead of " + taxLineItem.getTaxPercent(),
//                taxLineItem.getTaxPercent().compareTo(new BigDecimal(expectedPercent, mc)) == 0);
//    }

}
