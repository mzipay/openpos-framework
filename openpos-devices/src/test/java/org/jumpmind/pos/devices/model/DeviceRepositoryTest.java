package org.jumpmind.pos.devices.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import javax.sql.DataSource;

import org.jumpmind.pos.devices.TestDevicesApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = TestDevicesApplication.class)
public class DeviceRepositoryTest {

    @Autowired
    DeviceRepository repository;
    
    @Autowired
    DataSource dataSource;
    
    @Test
    public void testGetDevices() {
        Map<String, DeviceModel> map = repository.getDevices("dev");
        assertNotNull(map);
        assertEquals(2, map.size());
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
