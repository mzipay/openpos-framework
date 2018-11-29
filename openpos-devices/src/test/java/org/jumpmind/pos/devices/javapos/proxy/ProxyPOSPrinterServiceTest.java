package org.jumpmind.pos.devices.javapos.proxy;

import static org.junit.Assert.assertEquals;

import org.jumpmind.pos.core.javapos.SimulatedPOSPrinterService;
import org.jumpmind.pos.devices.TestDevicesApplication;
import org.jumpmind.pos.devices.javapos.EventCallbacksAdapter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import jpos.POSPrinterConst;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = TestDevicesApplication.class)
public class ProxyPOSPrinterServiceTest {

    @Test
    public void testPrint() throws Exception {
        ProxyPOSPrinterService clientProxyService = new ProxyPOSPrinterService();
        clientProxyService.profile = "dev";
        clientProxyService.port = 1973;
        EventCallbacksAdapter adapter = new EventCallbacksAdapter();
        clientProxyService.open("Printer", adapter);
        clientProxyService.claim(1000);
        clientProxyService.setDeviceEnabled(true);

        clientProxyService.printBitmap(POSPrinterConst.PTR_S_RECEIPT, "ReceiptLogo.jpg", POSPrinterConst.PTR_BM_ASIS,
                POSPrinterConst.PTR_BM_CENTER);
        clientProxyService.printNormal(POSPrinterConst.PTR_S_RECEIPT, "This is a test\n");
        clientProxyService.printNormal(POSPrinterConst.PTR_S_RECEIPT, "of the emergency broadcast system\n");
        clientProxyService.printBarCode(POSPrinterConst.PTR_S_RECEIPT, "1234567", POSPrinterConst.PTR_BCS_Code128, 50, 150,
                POSPrinterConst.PTR_BC_CENTER, POSPrinterConst.PTR_BC_TEXT_BELOW);
        clientProxyService.cutPaper(100);

        SimulatedPOSPrinterService serverPrinterService = SimulatedPOSPrinterService.instance;
        String receiptText = serverPrinterService.getLastReceipt();
        assertEquals(
                "This is a test\n" + "of the emergency broadcast system\n" + "1234567\n" + "\n" + "--------------- cut here ---------------",
                receiptText.trim());

    }

}
