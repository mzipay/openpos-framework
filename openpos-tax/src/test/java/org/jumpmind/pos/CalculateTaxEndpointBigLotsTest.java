package org.jumpmind.pos;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.jumpmind.pos.persist.cars.TestConfig;
import org.jumpmind.pos.tax.model.TaxAmount;
import org.jumpmind.pos.tax.model.TaxCalculationRequest;
import org.jumpmind.pos.tax.model.TaxCalculationResponse;
import org.jumpmind.pos.tax.model.TaxableItem;
import org.jumpmind.pos.tax.service.CalculateTaxEndpoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class})
public class CalculateTaxEndpointBigLotsTest {

	/* Designed for use with storedb in OrposToOpenposUtility */
	
	@Autowired
	private CalculateTaxEndpoint calculateTaxEndpoint;
    
	MathContext mc = new MathContext(10, RoundingMode.HALF_UP);
	
	@Test
    public void testGroup155AuthOHTransactionTax() {
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.addTaxableItem(getTaxableItem("155", 15.00));
        request.setGeoCode("00145");
        TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request);
        //TODO Add asserts
        System.out.println("Tax on $15.00 of General Apparel in OH is $" + response.getTaxAmount("100145", "155").getTaxAmount());
    }
	
	@Test
    public void testGroup155AuthFLTransactionTax() {
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.addTaxableItem(getTaxableItem("155", 15.00));
        request.setGeoCode("05277");
        TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request);
        //TODO Add asserts
        System.out.println("Tax on $15.00 of General Apparel in FL is $" + response.getTaxAmount("105277", "155").getTaxAmount());
    }
	
	@Test
    public void testGroup155AuthNYTransactionTax() {
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.addTaxableItem(getTaxableItem("155", 15.00));
        request.setGeoCode("00830");
        TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request);
        //TODO Add asserts
        System.out.println("Tax on $15.00 of General Apparel in NY is $" + response.getTaxAmount("200830", "155").getTaxAmount());
    }
	
	@Test
    public void testGroup145AuthFLTransactionTax() {
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.addTaxableItem(getTaxableItem("145", 149.99));
        request.setGeoCode("05277");
        TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request);
        //TODO Add asserts
        System.out.println("Tax on $149.99 of General Merchandise in FL is $" + response.getTaxAmount("105277", "145").getTaxAmount());
    }
	
	@Test
    public void testGroup145AuthOHTransactionTax() {
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.addTaxableItem(getTaxableItem("145", 149.99));
        request.setGeoCode("00145");
        TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request);
        //TODO Add asserts
        System.out.println("Tax on $149.99 of General Merchandise in OH is $" + response.getTaxAmount("100145", "145").getTaxAmount());
    }
	
	@Test
    public void testGroup305AuthOHTransactionTax() {
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.addTaxableItem(getTaxableItem("305", 12.00));
        request.setGeoCode("00145");
        TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request);
        //TODO Add asserts
        System.out.println("Tax on $12.00 of Energy Star CFL Bulbs in OH is $" + response.getTaxAmount("100145", "305").getTaxAmount());
    }
	
	/**
	 * Group Rule seems contradictory here
	 */
	@Test
    public void testGroup305AuthFLTransactionTax() {
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.addTaxableItem(getTaxableItem("305", 12.00));
        request.setGeoCode("05277");
        TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request);
        //TODO Add asserts
        System.out.println("Tax on $12.00 of Energy Star CFL Bulbs in FL is $" + response.getTaxAmount("105277", "305").getTaxAmount());
    }
	
	@Test
    public void testGroup214AuthMDTransactionTax() {
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.addTaxableItem(getTaxableItem("214", 15.99));
        request.setGeoCode("05291");
        TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request);
        //TODO Add asserts
        System.out.println("Tax on $15.99 of Alcohol in MD is $" + response.getTaxAmount("505291", "214").getTaxAmount());
    }
	
	@Test
    public void testGroup214AuthWATransactionTax() {
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.addTaxableItem(getTaxableItem("214", 15.99));
        request.setGeoCode("04644");
        TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request);
        //TODO Add asserts
        System.out.println("Tax on $15.99 of Alcohol in WA is $" + response.getTaxAmount("504644", "214").getTaxAmount());
    }
	
	/**
	 * Food tax in North Carolina
	 */
	@Test
    public void testGroup173AuthNCTransactionTax() {
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.addTaxableItem(getTaxableItem("173", 2.00));
        request.setGeoCode("05280");
        TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request);
        //TODO Add asserts
        System.out.println("Tax on $2.00 of Food - General in NC is $" + response.getTaxAmount("205280", "173").getTaxAmount());
    }
	
	/**
	 * Mattress Recycling in Ohio ???
	 */
	@Test
    public void testGroup820AuthNVTransactionTax() {
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.addTaxableItem(getTaxableItem("515", 0.00));
        request.setGeoCode("01969");
        TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request);
        //TODO Add asserts
        System.out.println("Tax on $0.00 of Mattress Recycling in OH is $" + response.getTaxAmount("101969", "515").getTaxAmount());
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
