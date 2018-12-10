package org.jumpmind.pos.devices.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jumpmind.pos.core.javapos.SimulatedScannerService;
import org.jumpmind.pos.devices.client.ProxyScannerService;
import org.jumpmind.pos.devices.javapos.EventCallbacksAdapter;
import org.jumpmind.test.TestDevicesApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import jpos.events.DataEvent;
import jpos.services.EventCallbacks;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = TestDevicesApplication.class)
public class ProxyScannerServiceTest {

    @Test
    public void testScan() throws Exception {
        ProxyScannerService clientProxyService = new ProxyScannerService();
        clientProxyService.profile = "dev";
        clientProxyService.port = 1975;
        EventCallbacksAdapter adapter = new EventCallbacksAdapter();
        clientProxyService.open("Scanner", adapter);
        clientProxyService.claim(1000);
        clientProxyService.setDeviceEnabled(true);
        clientProxyService.setDataEventEnabled(true);
        
        final String scanData = "this was a test of the emergency broadcast system";
        final int scanType = 105;
        
        SimulatedScannerService simulatedServerDevice = SimulatedScannerService.instance;
        simulatedServerDevice.setScanData(scanData.getBytes());
        simulatedServerDevice.setScanDateType(scanType);        
        EventCallbacks serverCallbacks = simulatedServerDevice.getCallbacks(); 
        serverCallbacks.fireDataEvent(new DataEvent(serverCallbacks.getEventSource(), 1));
        
        DataEvent event = adapter.waitForDataEvent(1000);
        assertNotNull("Never received the scan event", event);
        
        assertEquals(scanData, new String(clientProxyService.getScanData()));
        assertEquals(scanType, clientProxyService.getScanDataType());
    }

}
