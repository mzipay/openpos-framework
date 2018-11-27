package org.jumpmind.pos.devices.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import javax.sql.DataSource;

import org.jumpmind.pos.devices.DevicesModule;
import org.jumpmind.pos.devices.TestDevicesConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestDevicesConfig.class})
public class DeviceRepositoryTest {

    @Autowired
    DeviceRepository repository;
    
    @Autowired
    DataSource dataSource;
    
    @Autowired
    DevicesModule module;
    
    @Before
    public void init() {
        module.start();
    }
    
    @Test
    public void testGetDevices() {
        Map<String, DeviceModel> map = repository.getDevices("dev");
        assertNotNull(map);
        assertEquals(1, map.size());
        DeviceModel scanner = map.get("Scanner");
        assertNotNull(scanner);
        assertNotNull(scanner.getProperties());
        assertEquals(2, scanner.getProperties().size());
    }
    
    @Test
    public void testGetDevicesNothingFound() {
        Map<String, DeviceModel> map = repository.getDevices("nothing");
        assertNotNull(map);
        assertEquals(0, map.size());
    }

    
}
