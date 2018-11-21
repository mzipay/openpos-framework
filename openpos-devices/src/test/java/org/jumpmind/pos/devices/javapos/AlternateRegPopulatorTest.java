package org.jumpmind.pos.devices.javapos;

import org.jumpmind.pos.devices.DeviceModule;
import org.jumpmind.pos.devices.TestDevicesConfig;
import org.jumpmind.pos.devices.service.DeviceCache;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import jpos.Scanner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestDevicesConfig.class})
public class AlternateRegPopulatorTest {

    @Autowired
    DeviceCache cache;
    
    @Autowired
    DeviceModule module;
    
    @Before
    public void init() {
        module.start();
        cache.populate("dev");
    }
    
    @Test
    public void testInitializeScanner() throws Exception {
        Scanner scanner = new Scanner();
        scanner.open("Scanner");
    }
    

    
}
