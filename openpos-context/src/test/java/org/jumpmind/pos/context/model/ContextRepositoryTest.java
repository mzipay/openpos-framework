package org.jumpmind.pos.context.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import org.jumpmind.pos.persist.cars.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class})
public class ContextRepositoryTest {
    
    @Autowired
    ContextRepository contextRepository;

    @Test
    public void testFindDevice() {
        {            
            DeviceModel device = contextRepository.findDevice("100-1");
            assertNotNull(device);
            assertEquals("100-1", device.getDeviceId());
            assertEquals("WORKSTATION", device.getDeviceType());
            assertEquals("Store 100 Register 1", device.getDescription());
            assertEquals("N_AMERICA", device.getTagValue("REGION"));
            assertEquals("US", device.getTagValue("COUNTRY"));
            assertEquals("OH", device.getTagValue("STATE"));
            assertEquals("100", device.getTagValue("STORE_NUMBER"));
            assertEquals("REGULAR", device.getTagValue("STORE_TYPE"));
            assertEquals("WORKSTATION", device.getTagValue("DEVICE_TYPE"));
            assertEquals("Metl", device.getTagValue("BRAND_ID"));
            assertEquals("POS", device.getTagValue("APP_PROFILE"));
        }
    }
    @Test
    public void findDevicesByTag() {
        {            
            List<DeviceModel> devices = contextRepository.findDevicesByTag("STORE_NUMBER", "500");
            assertNotNull(devices);
            assertEquals(2, devices.size());
            {
                DeviceModel device = devices.get(0);
                assertEquals("WORKSTATION", device.getDeviceType());
                assertEquals("Store 500 Register 1", device.getDescription());
                assertEquals("N_AMERICA", device.getTagValue("REGION"));
                assertEquals("US", device.getTagValue("COUNTRY"));
                assertEquals("CA", device.getTagValue("STATE"));
                assertEquals("500", device.getTagValue("STORE_NUMBER"));
                assertEquals("POPUP", device.getTagValue("STORE_TYPE"));
                assertEquals("WORKSTATION", device.getTagValue("DEVICE_TYPE"));
                assertEquals("Metl", device.getTagValue("BRAND_ID"));
                assertEquals("POS", device.getTagValue("APP_PROFILE"));
            }
            {
                DeviceModel device = devices.get(1);
                assertEquals("WORKSTATION", device.getDeviceType());
                assertEquals("Store 500 Register 2", device.getDescription());
                assertEquals("N_AMERICA", device.getTagValue("REGION"));
                assertEquals("US", device.getTagValue("COUNTRY"));
                assertEquals("CA", device.getTagValue("STATE"));
                assertEquals("500", device.getTagValue("STORE_NUMBER"));
                assertEquals("POPUP", device.getTagValue("STORE_TYPE"));
                assertEquals("WORKSTATION", device.getTagValue("DEVICE_TYPE"));
                assertEquals("Metl", device.getTagValue("BRAND_ID"));
                assertEquals("POS", device.getTagValue("APP_PROFILE"));
            }
        }
    }
    
    @Test
    public void testLoadConfigs() {
        List<ConfigModel> welcomeTextConfigs = contextRepository.findConfigs(new Date(), "pos.welcome.text");
        assertEquals(11,  welcomeTextConfigs.size());
        for (ConfigModel configModel : welcomeTextConfigs) {
            assertEquals("pos.welcome.text", configModel.getConfigName());
        }
        
        assertEquals("Welcome global!", welcomeTextConfigs.get(0).getConfigValue());
        assertEquals("Welcome store 100 in OH, USA!", welcomeTextConfigs.get(10).getConfigValue());
    }
    
    @Test 
    public void testLoadTags() throws Exception {
        TagConfig tagConfig = ContextRepository.getTagConfig();
        assertEquals(8, tagConfig.getTags().size());
        
        assertEquals("REGION", tagConfig.getTags().get(0).getName());
        assertEquals("LOCATION", tagConfig.getTags().get(0).getGroup());
        
        assertEquals("STATE", tagConfig.getTags().get(2).getName());
        assertEquals("LOCATION", tagConfig.getTags().get(2).getGroup());        
        assertEquals(3, tagConfig.getTags().get(2).getLevel());
        
        assertEquals("APP_PROFILE", tagConfig.getTags().get(7).getName());        
    }
}
