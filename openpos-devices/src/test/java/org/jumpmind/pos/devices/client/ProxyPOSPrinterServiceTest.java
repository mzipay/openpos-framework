package org.jumpmind.pos.devices.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jumpmind.pos.core.javapos.SimulatedPOSPrinterService;
import org.jumpmind.pos.devices.client.ProxyPOSPrinterService;
import org.jumpmind.pos.devices.javapos.EventCallbacksAdapter;
import org.jumpmind.test.TestDevicesApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import jpos.POSPrinterConst;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = TestDevicesApplication.class)
public class ProxyPOSPrinterServiceTest {

    ProxyPOSPrinterService clientProxyService;

    @Before
    public void setup() throws Exception {
        clientProxyService = new ProxyPOSPrinterService();
        clientProxyService.profile = "dev";
        clientProxyService.port = 1975;
        EventCallbacksAdapter adapter = new EventCallbacksAdapter();
        clientProxyService.open("Printer", adapter);
        clientProxyService.claim(1000);
        clientProxyService.setDeviceEnabled(true);
    }

    @After
    public void tearDown() throws Exception {
        clientProxyService.close();
    }

    @Test
    public void testPrint() throws Exception {
        clientProxyService.printBitmap(POSPrinterConst.PTR_S_RECEIPT, "ReceiptLogo.jpg", POSPrinterConst.PTR_BM_ASIS,
                POSPrinterConst.PTR_BM_CENTER);
        clientProxyService.printNormal(POSPrinterConst.PTR_S_RECEIPT, "This is a test\n");
        clientProxyService.printNormal(POSPrinterConst.PTR_S_RECEIPT, "of the emergency broadcast system\n");
        clientProxyService.printBarCode(POSPrinterConst.PTR_S_RECEIPT, "1234567", POSPrinterConst.PTR_BCS_Code128, 50, 150,
                POSPrinterConst.PTR_BC_CENTER, POSPrinterConst.PTR_BC_TEXT_BELOW);
        clientProxyService.cutPaper(100);

        assertReceiptPrinted(
                "This is a test\n" + "of the emergency broadcast system\n" + "1234567\n" + "\n" + "--------------- cut here ---------------");

    }

    @Test
    public void testSlipPrint() throws Exception {
        clientProxyService.beginInsertion(1000);
        
        assertReceiptPrinted("-------------- begin insertion --------------");
        clientProxyService.endInsertion();
        assertReceiptPrinted("-------------- end insertion --------------");
        clientProxyService.printNormal(POSPrinterConst.PTR_S_SLIP, "output\n");
        clientProxyService.beginRemoval(1000);
        assertReceiptPrinted("output\n" + 
                "\n" + 
                "\n" + 
                "-------------- begin removal --------------");
        clientProxyService.endRemoval();
        assertReceiptPrinted("-------------- end removal --------------");


    }
    
    protected void assertReceiptPrinted(String expected) {
        SimulatedPOSPrinterService serverPrinterService = SimulatedPOSPrinterService.instance;
        String receiptText = serverPrinterService.getLastReceipt();
        assertNotNull("The receipt text was null", receiptText);
        assertEquals(
                expected,
                receiptText.trim());
        
    }

}
