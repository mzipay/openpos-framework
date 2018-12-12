package org.jumpmind.pos.devices.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import javax.sql.DataSource;

import org.jumpmind.test.TestDevicesApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = TestDevicesApplication.class)
public class DeviceRepositoryTest {

    @Autowired
    DevicesRepository repository;
    
    @Autowired
    DataSource dataSource;
    
    @Test
    public void testGetDevices() {
        Map<String, DeviceModel> map = repository.getDevices();
        assertNotNull(map);
        assertEquals(3, map.size());
        DeviceModel scanner = map.get("dev_Scanner");
        assertNotNull(scanner);
        assertNotNull(scanner.getProperties());
        assertEquals(2, scanner.getProperties().size());
    }
    
    
}
