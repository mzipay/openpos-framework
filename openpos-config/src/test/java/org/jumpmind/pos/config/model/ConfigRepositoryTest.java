package org.jumpmind.pos.config.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jumpmind.pos.config.ConfigException;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConfigRepositoryTest {
    
    private static ConfigRepository configRespository;
    
    @BeforeClass
    public static void setup() throws Exception {
        configRespository = new ConfigRepository();
        ConfigTestUtils.initTestDB(configRespository, "/config-test-data.sql");
    }
    
    @Test
    public void testSimpleConfigLoad() {
        ConfigModel config = configRespository.findConfigValue(new Date(), ConfigTestUtils.getTestNodeTags(), "config.string");
        assertEquals("ALL string value", config.getConfigValue());
    }
    @Test
    public void testIntConfigLoad() {
        ConfigModel config = configRespository.findConfigValue(new Date(), ConfigTestUtils.getTestNodeTags(), "config.int");
        assertEquals("500", config.getConfigValue());
    }
    @Test
    public void testConfigEnabled() {
        ConfigModel config = configRespository.findConfigValue(new Date(), ConfigTestUtils.getTestNodeTags(), "config.enabled.test");
        assertEquals("this one is ENABLED.", config.getConfigValue());
    }
    @Test
    public void testEffectiveExpireDates() {
        ConfigModel config = configRespository.findConfigValue(new Date(), ConfigTestUtils.getTestNodeTags(), "config.effective.test");
        assertEquals("this one should be active.", config.getConfigValue());
    }
    
    @Test(expected = ConfigException.class)
    public void testMostSpecificConfigNullValues1() {
        ConfigRepository configRepo = new ConfigRepository();
        configRepo.findMostSpecificConfig(null, null);
    }
    @Test(expected = ConfigException.class)
    public void testMostSpecificConfigNullValues2() {
        ConfigRepository configRepo = new ConfigRepository();
        configRepo.findMostSpecificConfig(new HashMap<>(), null);
    }
    @Test(expected = ConfigException.class)
    public void testMostSpecificConfigNullValues3() {
        ConfigRepository configRepo = new ConfigRepository();
        configRepo.findMostSpecificConfig(null, new ArrayList<>());
    }

    @Test
    public void testMostSpecificConfig1() {
        ConfigRepository configRepo = new ConfigRepository();
        
        Map<String, String> tags = new HashMap<>();
        tags.put(ConfigRepository.TAG_REGION, "NAM");
        tags.put(ConfigRepository.TAG_BRAND_ID, "JUSTICE");
        
        List<ConfigModel> configs = new ArrayList<>();
        {            
            ConfigModel config = new ConfigModel();
            config.setLocationType(LocationType.REGION);
            config.setLocationValue("NAM");
            config.setConfigName("test.config");
            config.setConfigValue("NAM_VALUE");
            configs.add(config);
        }
        {            
            ConfigModel config = new ConfigModel();
            config.setBrandId("JUSTICE");
            config.setConfigName("test.config");
            config.setConfigValue("JUSTICE_VALUE");
            configs.add(config);            
        }
        
        ConfigModel config = configRepo.findMostSpecificConfig(tags, configs);
        assertNotNull(config);
        assertEquals("JUSTICE_VALUE", config.getConfigValue());
    }
    
    @Test
    public void testMostSpecificConfig2() {
        ConfigRepository configRepo = new ConfigRepository();
        
        Map<String, String> tags = new HashMap<>();
        tags.put(ConfigRepository.TAG_REGION, "NAM");
        tags.put(ConfigRepository.TAG_STORE, "101");
        tags.put(ConfigRepository.TAG_BRAND_ID, "JUSTICE");
        
        List<ConfigModel> configs = new ArrayList<>();
        {   
            ConfigModel config = new ConfigModel();
            config.setLocationType(LocationType.REGION);
            config.setLocationValue("NAM");
            config.setConfigName("test.config");
            config.setConfigValue("NAM_VALUE");
            configs.add(config);
        }
        {            
            ConfigModel config = new ConfigModel();
            config.setLocationType(LocationType.STORE);
            config.setLocationValue("101");
            config.setConfigName("test.config");
            config.setConfigValue("101_VALUE");
            configs.add(config);
        }
        {            
            ConfigModel config = new ConfigModel();
            config.setBrandId("JUSTICE");
            config.setConfigName("test.config");
            config.setConfigValue("JUSTICE_VALUE");
            configs.add(config);            
        }
        {            
            ConfigModel config = new ConfigModel();
            config.setLocationType(LocationType.STORE);
            config.setLocationValue("101");
            config.setBrandId("JUSTICE");
            config.setConfigName("test.config");
            config.setConfigValue("101_JUSTICE_VALUE");
            configs.add(config);            
        }
        
        ConfigModel config = configRepo.findMostSpecificConfig(tags, configs);
        assertNotNull(config);
        assertEquals("101_JUSTICE_VALUE", config.getConfigValue());
    }
    
    
    @Test
    public void testChooseCorrectConfig() {
        ConfigRepository configRepo = new ConfigRepository();
        
        Map<String, String> tags = new HashMap<>();
        tags.put(ConfigRepository.TAG_REGION, "AMEA");
        tags.put(ConfigRepository.TAG_STORE, "101");
        tags.put(ConfigRepository.TAG_BRAND_ID, "JUSTICE");
        
        List<ConfigModel> configs = new ArrayList<>();
        {   
            ConfigModel config = new ConfigModel();
            config.setLocationType(LocationType.REGION);
            config.setLocationValue("NAM");
            config.setConfigName("test.config");
            config.setConfigValue("NAM_VALUE");
            configs.add(config);
        }
        {            
            ConfigModel config = new ConfigModel();
            config.setConfigName("test.config");
            config.setConfigValue("*_VALUE");
            configs.add(config);
        }
        
        ConfigModel config = configRepo.findMostSpecificConfig(tags, configs);
        assertNotNull(config);
        assertEquals("*_VALUE", config.getConfigValue());
    }
    
    @Test
    public void testTieBreak1() {
        ConfigRepository configRepo = new ConfigRepository();
        
        Map<String, String> tags = new HashMap<>();
        tags.put(ConfigRepository.TAG_BRAND_ID, "JUSTICE");
        tags.put(ConfigRepository.TAG_DEPARTMENT_ID, "FOOTWEAR");
        tags.put(ConfigRepository.TAG_STORE_TYPE, "OUTLET");
        tags.put(ConfigRepository.TAG_DEVICE_TYPE, "MOBILE");
        
        List<ConfigModel> configs = new ArrayList<>();
        {   
            ConfigModel config = new ConfigModel();
            config.setBrandId("JUSTICE");
            config.setConfigName("test.config");
            config.setConfigValue("JUSTICE_VALUE");
            configs.add(config);
        }
        {            
            ConfigModel config = new ConfigModel();
            config.setDepartmentId("FOOTWEAR");
            config.setConfigName("test.config");
            config.setConfigValue("FOOTWEAR_VALUE");
            configs.add(config);
        }
        
        ConfigModel config = configRepo.findMostSpecificConfig(tags, configs);
        assertNotNull(config);
        assertEquals("JUSTICE_VALUE", config.getConfigValue());
    }
    
    
    @Test
    public void testTieBreak2() {
        ConfigRepository configRepo = new ConfigRepository();
        
        Map<String, String> tags = new HashMap<>();
        tags.put(ConfigRepository.TAG_BRAND_ID, "JUSTICE");
        tags.put(ConfigRepository.TAG_DEPARTMENT_ID, "FOOTWEAR");
        tags.put(ConfigRepository.TAG_STORE_TYPE, "OUTLET");
        tags.put(ConfigRepository.TAG_DEVICE_TYPE, "MOBILE");
        
        List<ConfigModel> configs = new ArrayList<>();
        {            
            ConfigModel config = new ConfigModel();
            config.setDepartmentId("FOOTWEAR");
            config.setConfigName("test.config");
            config.setConfigValue("FOOTWEAR_VALUE");
            configs.add(config);
        }
        {            
            ConfigModel config = new ConfigModel();
            config.setStoreType("OUTLET");
            config.setConfigName("test.config");
            config.setConfigValue("OUTLET_VALUE");
            configs.add(config);
        }
        
        ConfigModel config = configRepo.findMostSpecificConfig(tags, configs);
        assertNotNull(config);
        assertEquals("OUTLET_VALUE", config.getConfigValue());
    }    
    
    @Test
    public void testTieBreak3() {
        ConfigRepository configRepo = new ConfigRepository();
        
        Map<String, String> tags = new HashMap<>();
        tags.put(ConfigRepository.TAG_BRAND_ID, "JUSTICE");
        tags.put(ConfigRepository.TAG_DEPARTMENT_ID, "FOOTWEAR");
        tags.put(ConfigRepository.TAG_STORE_TYPE, "OUTLET");
        tags.put(ConfigRepository.TAG_DEVICE_TYPE, "MOBILE");
        
        List<ConfigModel> configs = new ArrayList<>();
        {   
            ConfigModel config = new ConfigModel();
            config.setBrandId("JUSTICE");
            config.setConfigName("test.config");
            config.setConfigValue("JUSTICE_VALUE");
            configs.add(config);
        }
        {            
            ConfigModel config = new ConfigModel();
            config.setStoreType("OUTLET");
            config.setConfigName("test.config");
            config.setConfigValue("OUTLET_VALUE");
            configs.add(config);
        }
        
        ConfigModel config = configRepo.findMostSpecificConfig(tags, configs);
        assertNotNull(config);
        assertEquals("JUSTICE_VALUE", config.getConfigValue());
    }    
    
    @Test
    public void testTieBreak4() {
        ConfigRepository configRepo = new ConfigRepository();
        
        Map<String, String> tags = new HashMap<>();
        tags.put(ConfigRepository.TAG_BRAND_ID, "JUSTICE");
        tags.put(ConfigRepository.TAG_DEPARTMENT_ID, "FOOTWEAR");
        tags.put(ConfigRepository.TAG_STORE_TYPE, "OUTLET");
        tags.put(ConfigRepository.TAG_DEVICE_TYPE, "MOBILE");
        
        List<ConfigModel> configs = new ArrayList<>();
        {   
            ConfigModel config = new ConfigModel();
            config.setBrandId("JUSTICE");
            config.setConfigName("test.config");
            config.setConfigValue("JUSTICE_VALUE");
            configs.add(config);
        }
        {            
            ConfigModel config = new ConfigModel();
            config.setDeviceType("MOBILE");
            config.setConfigName("test.config");
            config.setConfigValue("MOBILE_VALUE");
            configs.add(config);
        }
        
        ConfigModel config = configRepo.findMostSpecificConfig(tags, configs);
        assertNotNull(config);
        assertEquals("JUSTICE_VALUE", config.getConfigValue());
    }    
    
    @Test
    public void testWeakWinner() {
        ConfigRepository configRepo = new ConfigRepository();
        
        Map<String, String> tags = new HashMap<>();
        tags.put(ConfigRepository.TAG_BRAND_ID, "JUSTICE");
        tags.put(ConfigRepository.TAG_DEPARTMENT_ID, "FOOTWEAR");
        tags.put(ConfigRepository.TAG_STORE_TYPE, "OUTLET");
        tags.put(ConfigRepository.TAG_DEVICE_TYPE, "MOBILE");
        
        List<ConfigModel> configs = new ArrayList<>();
        {   
            ConfigModel config = new ConfigModel();
            config.setConfigName("test.config");
            config.setConfigValue("JUSTICE_VALUE");
            configs.add(config);
        }
        {            
            ConfigModel config = new ConfigModel();
            config.setDeviceType("MOBILE");
            config.setConfigName("test.config");
            config.setConfigValue("MOBILE_VALUE");
            configs.add(config);
        }
        
        ConfigModel config = configRepo.findMostSpecificConfig(tags, configs);
        assertNotNull(config);
        assertEquals("MOBILE_VALUE", config.getConfigValue());
    }
    
    @Test
    public void testNoMatch() {
        ConfigRepository configRepo = new ConfigRepository();
        
        Map<String, String> tags = new HashMap<>();
        tags.put(ConfigRepository.TAG_BRAND_ID, "JUSTICE");
        tags.put(ConfigRepository.TAG_DEPARTMENT_ID, "FOOTWEAR");
        tags.put(ConfigRepository.TAG_STORE_TYPE, "OUTLET");
        tags.put(ConfigRepository.TAG_DEVICE_TYPE, "MOBILE");
        
        List<ConfigModel> configs = new ArrayList<>();
        {   
            ConfigModel config = new ConfigModel();
            config.setStoreType("REGULAR");
            config.setConfigName("test.config");
            config.setConfigValue("JUSTICE_VALUE");
            configs.add(config);
        }
        {            
            ConfigModel config = new ConfigModel();
            config.setDeviceType("CASHWRAP");
            config.setConfigName("test.config");
            config.setConfigValue("CASHWRAP_VALUE");
            configs.add(config);
        }
        
        ConfigModel config = configRepo.findMostSpecificConfig(tags, configs);
        assertNull(config);
    }    
    
}
