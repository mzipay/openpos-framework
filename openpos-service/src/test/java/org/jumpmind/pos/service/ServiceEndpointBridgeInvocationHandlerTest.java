package org.jumpmind.pos.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.annotation.RequestMapping;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestServiceConfig.class })
public class ServiceEndpointBridgeInvocationHandlerTest {

    @Autowired
    ApplicationContext applicationContext;
    
    @Autowired
    EndpointInjector endpointInjector;
    
    @Autowired
    TestEndpoint endpoint;
    
    @Autowired
    TestEndpointOverride override;
    
    @Test
    public void testThatOverrideIsCalled() throws Throwable {
        assertEquals(0, endpoint.invokeCount);
        assertEquals(0, override.invokeCount);
        new ServiceEndpointBridgeInvocationHandler(applicationContext, endpointInjector).invoke(null, ITest.class.getMethod("test"), null);
        assertEquals(0, endpoint.invokeCount);
        assertEquals(1, override.invokeCount);
    }
    
    @RequestMapping("/this/is/a/test")
    interface ITest {
        public void test();
    }
    
    
}
