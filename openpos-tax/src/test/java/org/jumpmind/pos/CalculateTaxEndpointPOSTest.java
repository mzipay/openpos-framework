package org.jumpmind.pos;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

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
public class CalculateTaxEndpointPOSTest {

	/* Designed for use with storedb in OrposToOpenposUtility */
	
	@Autowired
	private CalculateTaxEndpoint calculateTaxEndpoint;
	
	private List<TaxCalculationResponse> responses;
	
	MathContext mc = new MathContext(10, RoundingMode.HALF_UP);
	
	private void updateResponses(String...geocodes) {
		
		if (this.responses == null) {
			this.responses = new ArrayList<>();
		} else {
			this.responses.clear();
		}
		
		for (String geocode : geocodes) {
	
			// 'FEIT LED 40W FAN 	810248147' - General
			TaxCalculationRequest request1 = new TaxCalculationRequest();
			request1.addTaxableItem(getTaxableItem("145", 10.00));
			request1.setGeoCode(geocode);
			this.responses.add(calculateTaxEndpoint.calculateTax(request1));

			if (geocode.equals("00830")) {
				
				// 'DEARFOAMS MENS MF ADJ SLIDE BL 		810251801' - Clothing
				TaxCalculationRequest request2 = new TaxCalculationRequest();
				request2.addTaxableItem(getTaxableItem("155", 10.00));
				request2.setGeoCode(geocode);
				this.responses.add(calculateTaxEndpoint.calculateTax(request2));
				
			} else {
				
				// 2 x '4 SISTERS ESPRSSO RST 24 OZ		810264073' - Food
				TaxCalculationRequest request2 = new TaxCalculationRequest();
				request2.addTaxableItem(getTaxableItem("173", 5.00));
				request2.addTaxableItem(getTaxableItem("173", 5.00));
				request2.setGeoCode(geocode);
				this.responses.add(calculateTaxEndpoint.calculateTax(request2));
			}
			
			// 'DNU CONOUISTA CAB 	810189100' - Alcohol
			TaxCalculationRequest request3 = new TaxCalculationRequest();
			request3.addTaxableItem(getTaxableItem("214", 9.99));
			request3.setGeoCode(geocode);
			this.responses.add(calculateTaxEndpoint.calculateTax(request3));
		}
	}

	@Test
    public void testGroup305AuthFLTransactionTax() {
		String geocode1 = "05277";
		
		// '60W NON DIMM 2PK 		810235018' 	- E.S. CFL Bulbs
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.addTaxableItem(getTaxableItem("305", 12.00));
        request.setGeoCode(geocode1);
        TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request);

        assertTax(response, '1' + geocode1, "305", 0.72);
    }
	

	@Test
    public void testDupGroup145AuthOHTransactionTax() {
		String geocode1 = "00145";
		
		// 'PINNADEL BARSTOOLS 		810325261'	- Gen. Merch.
		// 'PINNADEL BARSTOOLS 		810325261'	- Gen. Merch.
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.addTaxableItem(getTaxableItem("145", 349.99));
        request.addTaxableItem(getTaxableItem("145", 349.99));
        request.setGeoCode(geocode1);
        TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request);

        assertTax(response, 52.50);
        
	}
		
	/**
	 * Mattress Recycling in Ohio
	 */
	@Test
    public void testGroup515AuthOHTransactionTax() {
		String geocode1 = "01969";
		
		// 'MATTRESS RECYCLE FEE	810270295' 	- Matt. Rec.
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.addTaxableItem(getTaxableItem("515", 0.00));
        request.setGeoCode(geocode1);
        TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request);

        assertTax(response, 0.00);
    }
	
	@Test
    public void testGroupDOUBLEAuthNYTransactionTax() {
		String geocode1 = "00830";
		
		// 'MATTRESS RECYCLE FEE	810270295' 	- Matt. Rec.
		// 'PINNADEL BARSTOOLS 		810325261'	- Gen. Merch.
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.addTaxableItem(getTaxableItem("515", 0.00));
        request.addTaxableItem(getTaxableItem("145", 349.99));
        request.setGeoCode(geocode1);
        TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request);

        assertTax(response, 28.00);
    }
	
	@Test
    public void testGroupDOUBLEAuthOHTransactionTax() {
		String geocode1 = "01969";
		
		// 'MATTRESS RECYCLE FEE	810270295' 	- Matt. Rec.
		// 'PINNADEL BARSTOOLS 		810325261'	- Gen. Merch.
        TaxCalculationRequest request = new TaxCalculationRequest();
        request.addTaxableItem(getTaxableItem("515", 0.00));
        request.addTaxableItem(getTaxableItem("145", 349.99));
        request.setGeoCode(geocode1);
        TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request);

        assertTax(response, 23.62);
	}
	
	@Test
    public void testGroupTRIPLEAuthNYTransactionTax() {
		String geocode1 = "00830";
		
		// 'MATTRESS RECYCLE FEE	810270295' 	- Matt. Rec.
		// 'PINNADEL BARSTOOLS 		810325261'	- Gen. Merch.
		// 'LADIES NOVELTY PJ		810315357'	- Gen. Apparel
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
        TaxCalculationResponse response1 = calculateTaxEndpoint.calculateTax(request1);

        assertTax(response, 28.60);
        assertTax(response1, 29.20);
        
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
        assertTax(responses.get(i++), "1" + geocode2, "145", 0.84);
        assertTax(responses.get(i++), "2" + geocode2, "173", 0.00);
        assertTax(responses.get(i++), "5" + geocode2, "214", 0.84);
        
    }
	
	@Test
    public void testMDGeocodesTransactionTax() {
		String geocode1 = "05290";
		String geocode2 = "05291";
		updateResponses(geocode1, geocode2);
        int i = 0;
        assertTax(responses.get(i++), "1" + geocode1, "145", 0.60);
        assertTax(responses.get(i++), "2" + geocode1, "173", 0.00);
        assertTax(responses.get(i++), "5" + geocode1, "214", 0.90);
        assertTax(responses.get(i++), "1" + geocode2, "145", 0.60);
        assertTax(responses.get(i++), "2" + geocode2, "173", 0.00);
        assertTax(responses.get(i++), "5" + geocode2, "214", 0.90);
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
        assertTax(responses.get(i++), "1" + geocode1, "145", 0.60);
        assertTax(responses.get(i++), "2" + geocode1, "173", 0.00);
        assertTax(responses.get(i++), "5" + geocode1, "214", 0.60);
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
	
	@Test
	public void testLowPriceTax() {
		String geocode1 = "05280";
		
		// 'DM TOMATO SAUCE 8 OZ 		810183704' 	- Food
		TaxCalculationRequest request1 = new TaxCalculationRequest();
		request1.addTaxableItem(getTaxableItem("173", 0.33));
		request1.setGeoCode(geocode1);
		TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request1);
		assertTax(response, "2" + geocode1, "173", 0.01);
		
	}
	
	@Test
	public void testCloseRoundingLowTax() {
		String geocode1 = "04642";
		
		// 'GRAVY TRAIN BEED STRIPS 	810309868' 	- Gen. Merch
		TaxCalculationRequest request1 = new TaxCalculationRequest();
		request1.addTaxableItem(getTaxableItem("145", 0.44));
		request1.setGeoCode(geocode1);
		TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request1);
		assertTax(response, "1" + geocode1, "145", 0.03);
	}
	
	@Test
	public void testCloseRoundingUpTax() {
		String geocode1 = "04644";
		
		// 'VICTORY HD IPA 6 PK 12 OZ 	810194331' 	- Alcohol
		TaxCalculationRequest request1 = new TaxCalculationRequest();
		request1.addTaxableItem(getTaxableItem("214", 9.11));
		request1.setGeoCode(geocode1);
		TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request1);
		assertTax(response, "5" + geocode1, "214", 0.77);
	}
	
	@Test
	public void testCloseRoundingDownTax() {
		String geocode1 = "04642";
		
		// 'VICTORY HD IPA 6 PK 12 OZ 	810194331' 	- Alcohol
		TaxCalculationRequest request1 = new TaxCalculationRequest();
		request1.addTaxableItem(getTaxableItem("214", 9.11));
		request1.setGeoCode(geocode1);
		TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request1);
		assertTax(response, "5" + geocode1, "214", 0.70);
	}
	
	//TODO tax holiday
	//TODO tax exempt
	
	@Test
	public void testCloseRoundingFourthPlaceTax() {
		String geocode1 = "04642";
		
		// 'CHANTILLY GIFTBOX 5OZ DPR	810201008' 	- Gen. Merch
		TaxCalculationRequest request1 = new TaxCalculationRequest();
		request1.addTaxableItem(getTaxableItem("145", 2.00));
		request1.setGeoCode(geocode1);
		TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request1);
		assertTax(response, "1" + geocode1, "145", 0.15);
	}
	
	@Test
	public void testCloseRoundingAddedUpTax() {
		String geocode1 = "04642";
		
		// 'VICTORY HD IPA 6 PK 12 OZ 	810194331' 	- Alcohol
		TaxCalculationRequest request1 = new TaxCalculationRequest();
		request1.addTaxableItem(getTaxableItem("214", 9.11));
		request1.addTaxableItem(getTaxableItem("214", 9.11));
		request1.addTaxableItem(getTaxableItem("214", 9.11));
		request1.setGeoCode(geocode1);
		TaxCalculationResponse response = calculateTaxEndpoint.calculateTax(request1);
		assertTax(response, "5" + geocode1, "214", 2.11);
	}
	
	private TaxableItem getTaxableItem(String groupId, double amount) {
        TaxableItem taxableItem = new TaxableItem();
        taxableItem.setGroupId(groupId);
        taxableItem.setExtendedAmount(new BigDecimal(amount));
        return taxableItem;
    }
	
	private BigDecimal setScale(BigDecimal dec) {
		dec = dec.setScale(2, mc.getRoundingMode());
		return dec;
	}

	private void assertTax(TaxCalculationResponse tran, double expectedAmount) {
        BigDecimal tax = new BigDecimal(0);
        List<TaxAmount> amounts = tran.getTaxAmounts();
        for (TaxAmount taxAmount : amounts) {
			tax = tax.add(taxAmount.getTaxAmount());
		}
        tax = setScale(tax);
        BigDecimal exp = setScale(new BigDecimal(expectedAmount));
        assertTrue("Expected tax of " + exp + " instead of " + tax, tax.compareTo(exp)==0);
	}
	
    private void assertTax(TaxCalculationResponse tran, String authorityId, String groupId, double expectedAmount) {
        TaxAmount taxAmount = tran.getTaxAmount(authorityId, groupId);
        if (expectedAmount == 0 && taxAmount == null) {
        	assertTrue(true);
        } else {
        	assertNotNull("Expected tax for tax group " + authorityId + "-" + groupId, taxAmount);
        	assertTrue("Expected tax of " + expectedAmount + " instead of " + setScale(taxAmount.getTaxAmount()),
                setScale(taxAmount.getTaxAmount()).compareTo(setScale(new BigDecimal(expectedAmount, mc))) == 0);
        }
    }

}
