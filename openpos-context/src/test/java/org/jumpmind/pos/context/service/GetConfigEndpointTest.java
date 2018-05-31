package org.jumpmind.pos.context.service;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jumpmind.pos.context.ContextException;
import org.jumpmind.pos.persist.cars.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class})
public class GetConfigEndpointTest {
    
    @Autowired
    private GetConfigEndpoint endpoint;
    
    @Test
    public void testConfigBestMatch() {
        {            
            ConfigResult configResult = endpoint.getConfig("100-1", getDate("2018-05-31 00:00:00"), "pos.welcome.text");
            assertEquals("pos.welcome.text", configResult.getConfigName());
            assertEquals("Welcome store 100 in OH, USA!", configResult.getConfigValue());
        }
        {            
            ConfigResult configResult = endpoint.getConfig("500-1", getDate("2018-05-31 00:00:00"), "pos.welcome.text");
            assertEquals("pos.welcome.text", configResult.getConfigName());
            assertEquals("Welcome POS app!", configResult.getConfigValue());
        }
        {            
            ConfigResult configResult = endpoint.getConfig("900-10", getDate("2018-05-31 00:00:00"), "pos.welcome.text");
            assertEquals("pos.welcome.text", configResult.getConfigName());
            assertEquals("Welcome UG POS app!", configResult.getConfigValue());
        }
        {            
            ConfigResult configResult = endpoint.getConfig("00039-011", getDate("2018-05-31 00:00:00"), "pos.welcome.text");
            assertEquals("pos.welcome.text", configResult.getConfigName());
            assertEquals("Welcome super specific!", configResult.getConfigValue());
        }
    }
    
    @Test
    public void testConfigBadNode() {
        ConfigResult configResult = endpoint.getConfig("999888-1", getDate("2018-05-31 00:00:00"), "pos.welcome.text");
        
        assertNull(configResult.getConfigName());
        assertEquals("NOT_FOUND", configResult.getResultStatus());        
    }
    
    @Test
    public void testConfigNotFound() {
        ConfigResult configResult = endpoint.getConfig("100-1", getDate("2018-05-31 00:00:00"), "pos.test.config.not.found");
        
        assertNull(configResult.getConfigName());
        assertEquals("NOT_FOUND", configResult.getResultStatus());        
    }
    
    @Test
    public void testConfigOnlyGlobal() {
        ConfigResult configResult = endpoint.getConfig("100-1", getDate("2018-05-31 00:00:00"), "pos.login.timeout");
        assertEquals("pos.login.timeout", configResult.getConfigName());
        assertEquals("global login timeout", configResult.getConfigValue());        
    }
    
    @Test
    public void testConfigOnlySpecific() {
        {            
            ConfigResult configResult = endpoint.getConfig("100-1", getDate("2018-05-31 00:00:00"), "pos.login.retry.attempts");
            assertEquals("pos.login.retry.attempts", configResult.getConfigName());
            assertEquals("10", configResult.getConfigValue());        
        }
        {            
            ConfigResult configResult = endpoint.getConfig("900-10", getDate("2018-05-31 00:00:00"), "pos.login.retry.attempts");
            assertEquals("pos.login.retry.attempts", configResult.getConfigName());
            assertEquals("15", configResult.getConfigValue());        
        }
        {            
            ConfigResult configResult = endpoint.getConfig("500-1", getDate("2018-05-31 00:00:00"), "pos.login.retry.attempts");
            assertNull(configResult.getConfigName());
            assertEquals("NOT_FOUND", configResult.getResultStatus());        
        }
    }
    
    @Test
    public void testExpired() {
        {            
            ConfigResult configResult = endpoint.getConfig("100-1", getDate("2018-05-31 00:00:00"), "test.expired");
            assertNull(configResult.getConfigName());
            assertEquals("NOT_FOUND", configResult.getResultStatus());        
        }
        {            
            ConfigResult configResult = endpoint.getConfig("900-10", getDate("2018-05-31 00:00:00"), "test.expired.one.active");
            assertEquals("test.expired.one.active", configResult.getConfigName());
            assertEquals("Value is active.", configResult.getConfigValue());        
        }
    }    
    
    @Test
    public void testNotEffectiveYet() {
        {            
            ConfigResult configResult = endpoint.getConfig("100-1", getDate("2018-12-31 00:00:00"), "test.effective");
            assertNull(configResult.getConfigName());
            assertEquals("NOT_FOUND", configResult.getResultStatus());        
        }
        {            
            ConfigResult configResult = endpoint.getConfig("900-10", getDate("2018-05-31 00:00:00"), "test.one.effective");
            assertEquals("test.one.effective", configResult.getConfigName());
            assertEquals("this one is effective", configResult.getConfigValue());        
        }
    }    
    
    public Date getDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            throw new ContextException(e);
        }
    }

}
