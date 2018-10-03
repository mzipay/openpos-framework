package org.jumpmind.pos;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.jumpmind.pos.persist.cars.TestPersistCarsConfig;
import org.jumpmind.pos.tax.model.TaxAmount;
import org.jumpmind.pos.tax.model.TaxableItem;
import org.jumpmind.pos.tax.service.CalculateTaxEndpoint;
import org.jumpmind.pos.tax.service.TaxCalculationRequest;
import org.jumpmind.pos.tax.service.TaxCalculationResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestPersistCarsConfig.class})
public class CalculateTaxEndpointTest {
	
	@Autowired
	private CalculateTaxEndpoint calculateTaxEndpoint;
    
	MathContext mc = new MathContext(10, RoundingMode.HALF_UP);
	
    @Test
    public void testTransactionTax() {
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.addTaxableItem(getTaxableItem("10013", 4.99));
        request.addTaxableItem(getTaxableItem("10013", 2.99));
        request.setGeoCode("700010040");
        TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request);
        /* tax = (4.99 + 2.99) * .15; */
        assertTax(response, "8", "10013", 1.20);
    }
    
    @Test
    public void testTransactionTax2() {
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.addTaxableItem(getTaxableItem("110", 4.99));
        request.addTaxableItem(getTaxableItem("110", 2.99));
        request.setGeoCode("130010030");
        TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request);
        /* tax = (4.99 + 2.99) * .06; */
        assertTax(response, "7488", "110", .48);
    }
    
    @Test
    public void testTransactionTaxUnderMin() {
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.addTaxableItem(getTaxableItem("10005", 4.99));
        request.addTaxableItem(getTaxableItem("10005", 2.99));
        request.setGeoCode("30130210");
        TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request);
        /* tax = (4.99 + 2.99) * .06; */
        assertTax(response, "1253", "10005", 0);
    }
    
    private TaxableItem getTaxableItem(String groupId, double amount) {
        TaxableItem taxableItem = new TaxableItem();
        taxableItem.setGroupId(groupId);
        taxableItem.setExtendedAmount(new BigDecimal(amount));
        return taxableItem;
    }

    private void assertTax(TaxCalculationResponse tran, String authorityId, String groupId, double expectedAmount) {
        TaxAmount taxAmount = tran.getTaxAmount(authorityId, groupId);
        assertNotNull("Expected tax for tax group " + authorityId + "-" + groupId, taxAmount);
        assertTrue("Expected tax of " + expectedAmount + " instead of " + taxAmount.getTaxAmount(),
                taxAmount.getTaxAmount().compareTo(new BigDecimal(expectedAmount, mc)) == 0);
    }

    private void assertTax(TaxCalculationResponse tran, String authorityId, String groupId, double expectedAmount, double expectedPercent) {
        assertTax(tran, authorityId, groupId, expectedAmount);
        TaxAmount taxAmount = tran.getTaxAmount(authorityId, groupId);
        assertTrue("Expected percent of " + expectedPercent + " instead of " + taxAmount.getTaxPercent(),
                taxAmount.getTaxPercent().compareTo(new BigDecimal(expectedPercent, mc)) == 0);
    }
}
