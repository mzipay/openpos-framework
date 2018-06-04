package org.jumpmind.pos;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.jumpmind.pos.persist.cars.TestConfig;
import org.jumpmind.pos.tax.model.TaxAmount;
import org.jumpmind.pos.tax.model.TaxCalculationRequest;
import org.jumpmind.pos.tax.model.TaxCalculationResponse;
import org.jumpmind.pos.tax.model.TaxableItem;
import org.jumpmind.pos.tax.service.CalculateTaxEndpoint;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class})
public class CalculateTaxEndpointPOSTest {

	/* Designed for use with storedb in OrposToOpenposUtility */
	
	@Autowired
	private CalculateTaxEndpoint calculateTaxEndpoint;
	
	private List<TaxCalculationResponse> responses;
	
	private static PrintWriter out;
	
	MathContext mc = new MathContext(10, RoundingMode.HALF_UP);
	
	MathContext mcRound = new MathContext(2, RoundingMode.HALF_UP);
	
	@BeforeClass
	public static void createStream() {
		try {
			PrintWriter outStream = new PrintWriter(new File("./src/test/resources/taxResults.txt"));
			out = outStream;
			out.println("State\t\tGeocode\t\tAuthority\t\tGroup\t\t\tItem Cost\t\tTax");
			out.println();
		} catch (Exception e) {
			
		}
	}
	
	@AfterClass
	public static void closeStream() {
		if (out != null) {
			out.close();
		}
	}
	
	private void updateResponses(String...geocodes) {
		
		if (this.responses == null) {
			this.responses = new ArrayList<>();
		} else {
			this.responses.clear();
		}
		
		for (String geocode : geocodes) {
	
			// 'FEIT LED 40W FAN 810248147' - General
			TaxCalculationRequest request1 = new TaxCalculationRequest();
			request1.addTaxableItem(getTaxableItem("145", 10.00));
			request1.setGeoCode(geocode);
			this.responses.add(calculateTaxEndpoint.calculateTax(request1));

			if (geocode.equals("00830")) {
				
				// 'DEARFOAMS MENS MF ADJ SLIDE BL 810251801' - Clothing
				TaxCalculationRequest request2 = new TaxCalculationRequest();
				request2.addTaxableItem(getTaxableItem("155", 10.00));
				request2.setGeoCode(geocode);
				this.responses.add(calculateTaxEndpoint.calculateTax(request2));
				
			} else {
				
				// 2 x '4 SISTERS ESPRSSO RST 24 OZ 810264073' - Food
				TaxCalculationRequest request2 = new TaxCalculationRequest();
				request2.addTaxableItem(getTaxableItem("173", 5.00));
				request2.addTaxableItem(getTaxableItem("173", 5.00));
				request2.setGeoCode(geocode);
				this.responses.add(calculateTaxEndpoint.calculateTax(request2));
			}
			
			// 'DNU CONOUISTA CAB 810189100' - Alcohol
			TaxCalculationRequest request3 = new TaxCalculationRequest();
			request3.addTaxableItem(getTaxableItem("214", 9.99));
			request3.setGeoCode(geocode);
			this.responses.add(calculateTaxEndpoint.calculateTax(request3));
		}
	}

	/**
	 * Group Rule seems contradictory here
	 */
	@Test
    public void testGroup305AuthFLTransactionTax() {
		String geocode1 = "05277";
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.addTaxableItem(getTaxableItem("305", 12.00));
        request.setGeoCode(geocode1);
        TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request);
        BigDecimal tax = new BigDecimal(0);
        List<TaxAmount> amounts = response.getTaxAmounts();
        for (TaxAmount taxAmount : amounts) {
			tax = tax.add(taxAmount.getTaxAmount());
		}
        assertTax(response, '1' + geocode1, "305", 0.00);
    }
	

	@Test
    public void testDupGroup145AuthOHTransactionTax() {
		String geocode1 = "00145";
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.addTaxableItem(getTaxableItem("145", 349.99));
        request.addTaxableItem(getTaxableItem("145", 349.99));
        request.setGeoCode(geocode1);
        TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request);
        BigDecimal tax = new BigDecimal(0);
        List<TaxAmount> amounts = response.getTaxAmounts();
        for (TaxAmount taxAmount : amounts) {
			tax = tax.add(taxAmount.getTaxAmount());
		}
        assertTrue(tax.compareTo(new BigDecimal(52.50)) == 0);
        
	}
		
	/**
	 * Mattress Recycling in Ohio
	 */
	@Test
    public void testGroup515AuthOHTransactionTax() {
		String geocode1 = "01969";
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.addTaxableItem(getTaxableItem("515", 0.00));
        request.setGeoCode(geocode1);
        TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request);
        BigDecimal tax = new BigDecimal(0);
        List<TaxAmount> amounts = response.getTaxAmounts();
        for (TaxAmount taxAmount : amounts) {
			tax = tax.add(taxAmount.getTaxAmount());
		}
        assertTax(response, '1' + geocode1, "515", 0.00);
    }
	
	@Test
    public void testGroupDOUBLEAuthNYTransactionTax() {
		String geocode1 = "00830";
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.addTaxableItem(getTaxableItem("515", 0.00));
        request.addTaxableItem(getTaxableItem("145", 349.99));
        request.setGeoCode(geocode1);
        TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request);
        BigDecimal tax = new BigDecimal(0);
        List<TaxAmount> amounts = response.getTaxAmounts();
        for (TaxAmount taxAmount : amounts) {
			tax = tax.add(taxAmount.getTaxAmount());
		}
        assertTrue(tax.compareTo(new BigDecimal(28.00)) == 0);
    }
	
	@Test
    public void testGroupDOUBLEAuthOHTransactionTax() {
		String geocode1 = "01969";
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.addTaxableItem(getTaxableItem("515", 0.00));
        request.addTaxableItem(getTaxableItem("145", 349.99));
        request.setGeoCode(geocode1);
        TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request);
        BigDecimal tax = new BigDecimal(0);
        List<TaxAmount> amounts = response.getTaxAmounts();
        for (TaxAmount taxAmount : amounts) {
			tax = tax.add(taxAmount.getTaxAmount());
		}
        assertTrue(tax.compareTo(new BigDecimal(23.62)) == 0);
        
	}
	
	@Test
    public void testGroupTRIPLEAuthNYTransactionTax() {
		String geocode1 = "00830";
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.addTaxableItem(getTaxableItem("515", 0.00));
        request.addTaxableItem(getTaxableItem("145", 349.99));
        request.addTaxableItem(getTaxableItem("155", 15.00));
        request.setGeoCode(geocode1);
        
        TaxCalculationRequest request1 = new TaxCalculationRequest();
        request1.addTaxableItem(getTaxableItem("515", 0.00));
        request1.addTaxableItem(getTaxableItem("145", 349.99));
        request1.addTaxableItem(getTaxableItem("145", 15.00));
        request1.setGeoCode(geocode1);
        
        TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request);
        BigDecimal tax = new BigDecimal(0);
        List<TaxAmount> amounts = response.getTaxAmounts();
        for (TaxAmount taxAmount : amounts) {
			tax = tax.add(taxAmount.getTaxAmount());
		}
        
        TaxCalculationResponse response1 = calculateTaxEndpoint.calculateTax(request1);
        BigDecimal tax1 = new BigDecimal(0);
        amounts = response1.getTaxAmounts();
        for (TaxAmount taxAmount : amounts) {
			tax1 = tax1.add(taxAmount.getTaxAmount());
		}
        assertTrue(tax.compareTo(new BigDecimal(28.60)) == 0);
        assertTrue(tax1.compareTo(new BigDecimal(29.20)) == 0);
        
	}
	
	@Test
    public void testOHGeocodesTransactionTax() {
		String geocode1 = "00145";
		String geocode2 = "01969";
		String geocode3 = "05243";	
		updateResponses(geocode1, geocode2, geocode3);
        int i = 0;
        assertTax(responses.get(i++), "1" + geocode1, "145", 0.75);
        assertTax(responses.get(i++), "2" + geocode1, "173", 0.00);
        assertTax(responses.get(i++), "5" + geocode1, "214", 0.75);
        assertTax(responses.get(i++), "1" + geocode2, "145", 0.68);
        assertTax(responses.get(i++), "2" + geocode2, "173", 0.00);
        assertTax(responses.get(i++), "5" + geocode2, "214", 0.00);
        assertTax(responses.get(i++), "1" + geocode3, "145", 0.75);
        assertTax(responses.get(i++), "2" + geocode3, "173", 0.00);
        assertTax(responses.get(i++), "5" + geocode3, "214", 0.00);
	}
	
	//TODO Check tax rate rule for Ohio, WA, NV
	//TODO write cases with weird prices to test rounding
	//TODO Check TaxConatiner for raw NY, NC values
	
	@Test
    public void testNCGeocodesTransactionTax() {
		String geocode1 = "05280";
		String geocode2 = "05276";
		updateResponses(geocode1, geocode2);
        int i = 0;
        assertTax(responses.get(i++), "1" + geocode1, "145", 0.70);
        assertTax(responses.get(i++), "2" + geocode1, "173", 0.20);
        assertTax(responses.get(i++), "5" + geocode1, "214", 0.70);
        assertTax(responses.get(i++), "1" + geocode2, "145", 0.70);
        assertTax(responses.get(i++), "2" + geocode2, "173", 0.20);
        assertTax(responses.get(i++), "5" + geocode2, "214", 0.70);
     }
	
	@Test
    public void testWAGeocodesTransactionTax() {
        String geocode1 = "04646";
		String geocode2 = "04644";
        updateResponses(geocode1, geocode2);
        int i = 0;
        assertTax(responses.get(i++), "1" + geocode1, "145", 0.88);
        assertTax(responses.get(i++), "2" + geocode1, "173", 0.00);
        assertTax(responses.get(i++), "5" + geocode1, "214", 0.88);
        assertTax(responses.get(i++), "1" + geocode1, "145", 0.84);
        assertTax(responses.get(i++), "2" + geocode1, "173", 0.00);
        assertTax(responses.get(i++), "5" + geocode1, "214", 0.84);
    }
	
	@Test
    public void testMDGeocodesTransactionTax() {
		String geocode1 = "05290";
		String geocode2 = "05291";
		updateResponses(geocode1, geocode2);
        int i = 0;
        assertTax(responses.get(i++), "1" + geocode1, "145", 0.00);
        assertTax(responses.get(i++), "2" + geocode1, "173", 0.00);
        assertTax(responses.get(i++), "5" + geocode1, "214", 0.00);
        assertTax(responses.get(i++), "1" + geocode2, "145", 0.00);
        assertTax(responses.get(i++), "2" + geocode2, "173", 0.00);
        assertTax(responses.get(i++), "5" + geocode2, "214", 0.00);
    }
	
	@Test
    public void testNYGeocodesTransactionTax() {
		String geocode1 = "00830";
		updateResponses(geocode1);
        int i = 0;
        assertTax(responses.get(i++), "1" + geocode1, "145", 0.80);
        assertTax(responses.get(i++), "2" + geocode1, "155", 0.40);
        assertTax(responses.get(i++), "5" + geocode1, "214", 0.80);
        
	}
	
	@Test
    public void testNVGeocodesTransactionTax() {
		String geocode1 = "04642";
		updateResponses(geocode1);
        int i = 0;
        assertTax(responses.get(i++), "1" + geocode1, "145", 0.77);
        assertTax(responses.get(i++), "2" + geocode1, "173", 0.00);
        assertTax(responses.get(i++), "5" + geocode1, "214", 0.77);
	}
	
	@Test
    public void testFLGeocodesTransactionTax() {
		String geocode1 = "05277";
		updateResponses(geocode1);
        int i = 0;
        assertTax(responses.get(i++), "1" + geocode1, "145", 0.00);
        assertTax(responses.get(i++), "2" + geocode1, "173", 0.00);
        assertTax(responses.get(i++), "5" + geocode1, "214", 0.00);
	}
	
	@Test
    public void testCAGeocodesTransactionTax() {
		String geocode1 = "04061";
		updateResponses(geocode1);
        int i = 0;
        assertTax(responses.get(i++), "1" + geocode1, "145", 0.80);
        assertTax(responses.get(i++), "2" + geocode1, "173", 0.00);
        assertTax(responses.get(i++), "5" + geocode1, "214", 0.00);    
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
