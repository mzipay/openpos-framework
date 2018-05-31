package org.jumpmind.pos.context.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class TagCalculatorTest {
    
    public TagConfig buildTagConfig() {
        TagConfig tagConfig = new TagConfig();
        List<TagModel> tagModels = new ArrayList<>();
        {
            TagModel tagModel = new TagModel();
            tagModel.setGroup("LOCATION");
            tagModel.setName("REGION");
            tagModel.setLevel(1);
            tagModels.add(tagModel);
        }
        {
            TagModel tagModel = new TagModel();
            tagModel.setGroup("LOCATION");
            tagModel.setName("COUNTRY");
            tagModel.setLevel(2);
            tagModels.add(tagModel);
        }

        {
            TagModel tagModel = new TagModel();
            tagModel.setGroup("LOCATION");
            tagModel.setName("STATE");
            tagModel.setLevel(4);
            tagModels.add(tagModel);
        }
        {
            TagModel tagModel = new TagModel();
            tagModel.setName("STORE_NUMBER");
            tagModels.add(tagModel);
        }
        {
            TagModel tagModel = new TagModel();
            tagModel.setName("STORE_TYPE");
            tagModels.add(tagModel);
        }
        {
            TagModel tagModel = new TagModel();
            tagModel.setName("DEVICE_TYPE");
            tagModels.add(tagModel);
        }
        {
            TagModel tagModel = new TagModel();
            tagModel.setName("BRAND_ID");
            tagModels.add(tagModel);
        }
        {
            TagModel tagModel = new TagModel();
            tagModel.setName("APP_PROFILE");
            tagModels.add(tagModel);
        }
        
        tagConfig.setTags(tagModels);
        return tagConfig;
    }
    

    @Test
    public void testBasicDisqualified() {
        TagCalculator tagCalculator = new TagCalculator();
        
        List<ITaggedElement> taggedElements = new ArrayList<>();
        
        {            
            ConfigModel config = new ConfigModel();
            config.setTagValue("STORE_NUMBER", "*");
            config.setConfigName("test.config");
            config.setConfigValue("All stores config value");
            taggedElements.add(config);
        }
        {            
            ConfigModel config = new ConfigModel();
            config.setTagValue("STORE_NUMBER", "200");
            config.setConfigName("test.config");
            config.setConfigValue("store 200 config value");
            taggedElements.add(config);
        }
        
        Map<String, String> specifiedTags = getSpecifiedTags();
        
        ConfigModel element = (ConfigModel) tagCalculator.getMostSpecific(taggedElements, specifiedTags, buildTagConfig());
        assertEquals("All stores config value", element.getConfigValue());
    }
    
    @Test
    public void testBasicMatch() {
        TagCalculator tagCalculator = new TagCalculator();
        
        List<ITaggedElement> taggedElements = new ArrayList<>();
        
        {            
            ConfigModel config = new ConfigModel();
            config.setTagValue("STORE_NUMBER", "*");
            config.setConfigName("test.config");
            config.setConfigValue("All stores config value");
            taggedElements.add(config);
        }
        {            
            ConfigModel config = new ConfigModel();
            config.setTagValue("STORE_NUMBER", "100");
            config.setConfigName("test.config");
            config.setConfigValue("store 100 config value");
            taggedElements.add(config);
        }        
        {            
            ConfigModel config = new ConfigModel();
            config.setTagValue("STORE_NUMBER", "200");
            config.setConfigName("test.config");
            config.setConfigValue("store 200 config value");
            taggedElements.add(config);
        }
        
        Map<String, String> specifiedTags = getSpecifiedTags();
        
        ConfigModel element = (ConfigModel) tagCalculator.getMostSpecific(taggedElements, specifiedTags, buildTagConfig());
        assertEquals("store 100 config value", element.getConfigValue());
        
        specifiedTags.put("STORE_NUMBER", "200");
        element = (ConfigModel) tagCalculator.getMostSpecific(taggedElements, specifiedTags, buildTagConfig());
        assertEquals("store 200 config value", element.getConfigValue());        
    }
    
    
    @Test
    public void testPartialDisqualify() {
        TagCalculator tagCalculator = new TagCalculator();
        
        List<ITaggedElement> taggedElements = new ArrayList<>();
        
        {            
            ConfigModel config = new ConfigModel();
            config.setTagValue("STORE_NUMBER", "*");
            config.setConfigName("test.config");
            config.setConfigValue("All stores config value");
            taggedElements.add(config);
        }
        {            
            ConfigModel config = new ConfigModel();
            config.setTagValue("STORE_NUMBER", "100");
            config.setTagValue("STORE_TYPE", "outlet");
            config.setConfigName("test.config");
            config.setConfigValue("store 100 config value");
            taggedElements.add(config);
        }        
        {            
            ConfigModel config = new ConfigModel();
            config.setTagValue("STORE_NUMBER", "200");
            config.setTagValue("STORE_TYPE", "outlet");
            config.setConfigName("test.config");
            config.setConfigValue("store 200 config value");
            taggedElements.add(config);
        }
        
        Map<String, String> specifiedTags = getSpecifiedTags();
        
        ConfigModel element = (ConfigModel) tagCalculator.getMostSpecific(taggedElements, specifiedTags, buildTagConfig());
        assertEquals("All stores config value", element.getConfigValue());
        
        specifiedTags.put("STORE_NUMBER", "200");
        element = (ConfigModel) tagCalculator.getMostSpecific(taggedElements, specifiedTags, buildTagConfig());
        assertEquals("All stores config value", element.getConfigValue());        
    }
    
    @Test
    public void testNoMatch() {
        TagCalculator tagCalculator = new TagCalculator();
        
        List<ITaggedElement> taggedElements = new ArrayList<>();
        
        {            
            ConfigModel config = new ConfigModel();
            config.setTagValue("STORE_NUMBER", "*");
            config.setConfigName("test.config");
            config.setTagValue("STORE_TYPE", "outlet");
            config.setConfigValue("All stores config value");
            taggedElements.add(config);
        }
        {            
            ConfigModel config = new ConfigModel();
            config.setTagValue("STORE_NUMBER", "100");
            config.setTagValue("STORE_TYPE", "outlet");
            config.setConfigName("test.config");
            config.setConfigValue("store 100 config value");
            taggedElements.add(config);
        }        
        {            
            ConfigModel config = new ConfigModel();
            config.setTagValue("STORE_NUMBER", "200");
            config.setTagValue("STORE_TYPE", "outlet");
            config.setConfigName("test.config");
            config.setConfigValue("store 200 config value");
            taggedElements.add(config);
        }
        
        Map<String, String> specifiedTags = getSpecifiedTags();
        
        ConfigModel element = (ConfigModel) tagCalculator.getMostSpecific(taggedElements, specifiedTags, buildTagConfig());
        assertNull(element);
    }
    
    @Test
    public void testSingleMatch() {
        TagCalculator tagCalculator = new TagCalculator();
        
        List<ITaggedElement> taggedElements = new ArrayList<>();
        
        {            
            ConfigModel config = new ConfigModel();
            config.setTagValue("STORE_NUMBER", "*");
            config.setConfigName("test.config");
            config.setConfigValue("All stores config value");
            taggedElements.add(config);
        }
        
        Map<String, String> specifiedTags = getSpecifiedTags();
        
        ConfigModel element = (ConfigModel) tagCalculator.getMostSpecific(taggedElements, specifiedTags, buildTagConfig());
        assertEquals("All stores config value", element.getConfigValue());
        
        specifiedTags.put("STORE_NUMBER", "200");
        element = (ConfigModel) tagCalculator.getMostSpecific(taggedElements, specifiedTags, buildTagConfig());
        assertEquals("All stores config value", element.getConfigValue());
    }
    
    @Test
    public void testPerfectMatch() {
        TagCalculator tagCalculator = new TagCalculator();
        
        List<ITaggedElement> taggedElements = new ArrayList<>();
        
        {            
            ConfigModel config = new ConfigModel();
            config.setTagValue("REGION", "N_AMERICA");
            config.setTagValue("COUNTRY", "US");
            config.setTagValue("STATE", "OK");
            config.setTagValue("STORE_NUMBER", "100");
            config.setTagValue("STORE_TYPE", "REGULAR");
            config.setTagValue("BRAND_ID", "METL");
            config.setTagValue("APP_PROFILE", "ECOMM");
            config.setConfigName("test.config");
            config.setConfigValue("Disqualified");
            taggedElements.add(config);
        }
        {            
            ConfigModel config = new ConfigModel();
            config.setTagValue("REGION", "N_AMERICA");
            config.setTagValue("COUNTRY", "US");
            config.setTagValue("STATE", "OK");
            config.setTagValue("STORE_NUMBER", "100");
            config.setTagValue("STORE_TYPE", "REGULAR");
            config.setTagValue("BRAND_ID", "METL");
            config.setTagValue("APP_PROFILE", "ECOMM2");
            config.setConfigName("test.config");
            config.setConfigValue("Disqualified");
            taggedElements.add(config);
        }
        {            
            ConfigModel config = new ConfigModel();
            config.setTagValue("REGION", "N_AMERICA");
            config.setTagValue("COUNTRY", "US");
            config.setTagValue("STATE", "OK");
            config.setTagValue("STORE_NUMBER", "100");
            config.setTagValue("STORE_TYPE", "REGULAR");
            config.setTagValue("BRAND_ID", "METL");
            config.setTagValue("APP_PROFILE", "WAREHOUSE");
            config.setConfigName("test.config");
            config.setConfigValue("Disqualified");
            taggedElements.add(config);
        }
        {            
            ConfigModel config = new ConfigModel();
            config.setTagValue("REGION", "N_AMERICA");
            config.setTagValue("COUNTRY", "US");
            config.setTagValue("STATE", "OK");
            config.setTagValue("STORE_NUMBER", "100");
            config.setTagValue("STORE_TYPE", "REGULAR");
            config.setTagValue("BRAND_ID", "METL");
            config.setTagValue("APP_PROFILE", "POS");
            config.setConfigName("test.config");
            config.setConfigValue("The Chosen One");
            taggedElements.add(config);
        }
        
        Map<String, String> specifiedTags = getSpecifiedTags();
        
        ConfigModel element = (ConfigModel) tagCalculator.getMostSpecific(taggedElements, specifiedTags, buildTagConfig());
        assertEquals("The Chosen One", element.getConfigValue());
    }    
    
    @Test
    public void testCloseDisqualify() {
        TagCalculator tagCalculator = new TagCalculator();
        
        List<ITaggedElement> taggedElements = new ArrayList<>();
        
        {            
            ConfigModel config = new ConfigModel();
            config.setTagValue("REGION", "N_AMERICA");
            config.setTagValue("COUNTRY", "US");
            config.setTagValue("STATE", "OK");
            config.setTagValue("STORE_NUMBER", "100");
            config.setTagValue("STORE_TYPE", "REGULAR");
            config.setTagValue("BRAND_ID", "METL");
            config.setTagValue("APP_PROFILE", "ECOMM");
            config.setConfigName("test.config");
            config.setConfigValue("Disqualified");
            taggedElements.add(config);
        }
        {            
            ConfigModel config = new ConfigModel();
            config.setTagValue("REGION", "N_AMERICA");
            config.setTagValue("COUNTRY", "US");
            config.setTagValue("STATE", "OK");
            config.setTagValue("STORE_NUMBER", "100");
            config.setTagValue("STORE_TYPE", "REGULAR");
            config.setTagValue("BRAND_ID", "METL");
            config.setTagValue("APP_PROFILE", "ECOMM2");
            config.setConfigName("test.config");
            config.setConfigValue("Disqualified");
            taggedElements.add(config);
        }
        {            
            ConfigModel config = new ConfigModel();
            config.setTagValue("REGION", "N_AMERICA");
            config.setTagValue("COUNTRY", "US");
            config.setTagValue("STATE", "OK");
            config.setTagValue("STORE_NUMBER", "100");
            config.setTagValue("STORE_TYPE", "REGULAR");
            config.setTagValue("BRAND_ID", "METL");
            config.setTagValue("APP_PROFILE", "WAREHOUSE");
            config.setConfigName("test.config");
            config.setConfigValue("Disqualified");
            taggedElements.add(config);
        }
        
        Map<String, String> specifiedTags = getSpecifiedTags();
        
        ConfigModel element = (ConfigModel) tagCalculator.getMostSpecific(taggedElements, specifiedTags, buildTagConfig());
        assertNull(element);
    }    


    protected Map<String, String> getSpecifiedTags() {
        Map<String, String> specifiedTags = new HashMap<>();
        specifiedTags.put("REGION", "N_AMERICA");
        specifiedTags.put("COUNTRY", "US");
        specifiedTags.put("STATE", "OK");
        specifiedTags.put("STORE_NUMBER", "100");
        specifiedTags.put("STORE_TYPE", "REGULAR");
        specifiedTags.put("BRAND_ID", "METL");
        specifiedTags.put("APP_PROFILE", "POS");
        return specifiedTags;
    }
    
}
