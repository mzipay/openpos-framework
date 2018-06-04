package org.jumpmind.pos.context.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.jumpmind.pos.persist.cars.TestConfig;
import org.jumpmind.pos.service.PosServerException;
import org.jumpmind.pos.service.util.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.type.TypeReference;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {TestConfig.class})
public class ContextServiceClientTest {
    
    @Autowired
    private ContextService contextService;

    @Test
    public void testGetInt() {
        ContextServiceClient contextServiceClient = getContextServiceClient();
        assertEquals(500, contextServiceClient.getInt("test.integer"));
    }
    
    @Test(expected=PosServerException.class)
    public void testGetBadInt() {
        ContextServiceClient contextServiceClient = getContextServiceClient();
        assertEquals(500, contextServiceClient.getInt("test.integer.bad"));
    }
    
    @Test
    public void testGetLong() {
        ContextServiceClient contextServiceClient = getContextServiceClient();
        assertEquals(50000000000l, contextServiceClient.getLong("test.long"));
    }
    
    @Test(expected=PosServerException.class)
    public void testGetLongBad() {
        ContextServiceClient contextServiceClient = getContextServiceClient();
        assertEquals(50000000000l, contextServiceClient.getInt("test.long.bad"));
    }
    
    @Test
    public void testGetDate() {
        ContextServiceClient contextServiceClient = getContextServiceClient();
        assertEquals(DateUtils.parseDateTimeISO("2018-06-01"), contextServiceClient.getDate("test.date"));
    }
    
    @Test(expected=PosServerException.class)
    public void testGetDateBad() {
        ContextServiceClient contextServiceClient = getContextServiceClient();
        assertEquals(DateUtils.parseDateTimeISO("2018-06-01"), contextServiceClient.getInt("test.date.bad"));
    }
    
    @Test
    public void testGetDateSeconds() {
        ContextServiceClient contextServiceClient = getContextServiceClient();
        assertEquals(DateUtils.parseDateTimeISO("2018-06-01 16:25:32"), contextServiceClient.getDate("test.date.time.seconds"));
    }    
    
    @Test
    public void testGetDateMillis() {
        ContextServiceClient contextServiceClient = getContextServiceClient();
        assertEquals(DateUtils.parseDateTimeISO("2018-06-01 16:25:32.298"), contextServiceClient.getDate("test.date.time.ms"));
    }
    
    @Test
    public void testSimpleJson() {
        ContextServiceClient contextServiceClient = getContextServiceClient();
        SimpleJsonPojo expected = new SimpleJsonPojo();
        expected.setId("1234");
        expected.setSequence(888999);
        expected.setCreateDate(DateUtils.parseDateTimeISO("2018-06-01T17:45:40"));
        SimpleJsonPojo actual = contextServiceClient.getObject("test.simple.json", SimpleJsonPojo.class); 
        assertEquals(expected, actual);
    }    
    
    @Test
    public void testArrayJson() {
        ContextServiceClient contextServiceClient = getContextServiceClient();
        List<SimpleJsonPojo> expected = new ArrayList<>();
        
        {            
            SimpleJsonPojo expectedElement = new SimpleJsonPojo();
            expectedElement.setId("1234");
            expectedElement.setSequence(888999);
            expectedElement.setCreateDate(DateUtils.parseDateTimeISO("2018-06-01T17:45:40"));
            expected.add(expectedElement);
        }
        {            
            SimpleJsonPojo expectedElement = new SimpleJsonPojo();
            expectedElement.setId("56789");
            expectedElement.setSequence(888000);
            expectedElement.setCreateDate(DateUtils.parseDateTimeISO("2017-06-01T17:45:40"));
            expected.add(expectedElement);
        }
        
        List<SimpleJsonPojo> actual = contextServiceClient.getObjectList("test.array.json", SimpleJsonPojo.class); 
        assertEquals(expected, actual);
    }    
    
    @Test
    public void testDecimalJson() {
        ContextServiceClient contextServiceClient = getContextServiceClient();
        SimpleJsonPojo expected = new SimpleJsonPojo();
        expected.setId("1234");
        expected.setSequence(888999);
        expected.setCreateDate(DateUtils.parseDateTimeISO("2018-06-01T17:45:40"));
        SimpleJsonPojo actual = contextServiceClient.getObject("test.simple.json", SimpleJsonPojo.class); 
        assertEquals(expected, actual);        
    }
    
    protected ContextServiceClient getContextServiceClient() {
        ContextServiceClient contextServiceClient = new ContextServiceClient(contextService, "100-1");
        contextServiceClient.setConfigDate(DateUtils.parseDateTimeISO("2018-06-01 00:00:00"));
        return contextServiceClient;
    }
    
}
