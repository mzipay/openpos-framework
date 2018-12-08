package org.jumpmind.pos.devices.javapos;

import org.jumpmind.pos.devices.DevicesModule;
import org.jumpmind.pos.devices.service.DeviceCache;
import org.jumpmind.test.TestDevicesApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import jpos.Scanner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = TestDevicesApplication.class)
public class AlternateRegPopulatorTest {

    @Autowired
    DeviceCache cache;
    
    @Autowired
    DevicesModule module;
    
    @Before
    public void init() {
        module.start();
        cache.populate();
    }
    
    @Test
    public void testInitializeScanner() throws Exception {
        Scanner scanner = new Scanner();
        scanner.open("dev-Scanner");
    }
    

    
}
