package org.jumpmind.pos.core.screeninterceptor;

import org.jumpmind.pos.util.model.Message;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(MockitoJUnitRunner.class)
public class AbstractMessagePropertyCrawlerInterceptorTest {

    IMessagePropertyStrategy<TestMessage> mockStrategy;

    AbstractMessagePropertyCrawlerInterceptor<TestMessage> interceptor;
    
    TestMessage testMessage;
    
    @Before
    public void setUp() throws Exception {
        // Set up mock IMessagePropertyStrategy to do the following:
        // If the property being processed is a String and starts with "@" character, return hardcoded "foo"
        // else, for all other cases just return the given unmodified value back
        mockStrategy = (appId, deviceId, property, clazz, message, messageContext) -> {
            if (property != null && property instanceof String && property.toString().startsWith("@")) {
                return "foo";
            }
            else {
                return property;
            }
        };

        interceptor = new TestMessageCrawlerInterceptor(mockStrategy);
        testMessage = new TestMessage();
    }

    private Answer<Object> returnCurrentPropertyValue() {
        return new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArguments()[2];
            }
        };
    }

    /**
     * Ensure that we support crawling an array of Strings that are a top level
     * property of a given object (TestMessage).
     */
    @Test
    public void testStringArrayProperty() {
        testMessage.stringList = new String[] {"@replace-me", "dont-replace", "@do-replace"};
        interceptor.intercept("someApp", "someDevice", testMessage);
        assertEquals(3, testMessage.stringList.length);
        assertEquals("foo", testMessage.stringList[0]);
        assertEquals("dont-replace", testMessage.stringList[1]);
        assertEquals("foo", testMessage.stringList[2]);
    }

    /**
     * We don't support crawling an array of Objects that are not strings.
     */
    @Test
    public void testArbitraryObjectArrayProperty() {
        testMessage.arbitraryObjectArray = new ArbitraryClass[] {new ArbitraryClass("@should-not-be-replaced"), new ArbitraryClass("also-should-not-be")};
        interceptor.intercept("someApp", "someDevice", testMessage);
        assertEquals(2, testMessage.arbitraryObjectArray.length);
        assertEquals("@should-not-be-replaced", testMessage.arbitraryObjectArray[0].someProperty);
        assertEquals("also-should-not-be", testMessage.arbitraryObjectArray[1].someProperty);
    }

    /**
     * Ensure we support crawling a list of objects.
     */
    @Test
    public void testArbitraryObjectListProperty() {
        testMessage.arbitraryObjectList = Arrays.asList(new ArbitraryClass[] {new ArbitraryClass("@should-be-replaced"), new ArbitraryClass("should-not-be-replaced")});
        interceptor.intercept("someApp", "someDevice", testMessage);
        assertEquals(2, testMessage.arbitraryObjectList.size());
        assertEquals("foo", testMessage.arbitraryObjectList.get(0).someProperty);
        assertEquals("should-not-be-replaced", testMessage.arbitraryObjectList.get(1).someProperty);
    }

    /**
     * Ensure we support crawling a collection (that's not a list) of objects.
     */
    @Test
    public void testArbitraryObjectCollectionProperty() {
        testMessage.arbitraryObjectSet = new HashSet<>();
        testMessage.arbitraryObjectSet.add(new ArbitraryClass("@should-be-replaced"));
        testMessage.arbitraryObjectSet.add(new ArbitraryClass("should-not-be-replaced"));
        
        interceptor.intercept("someApp", "someDevice", testMessage);
        assertEquals(2, testMessage.arbitraryObjectSet.size());
        assertTrue(testMessage.arbitraryObjectSet.stream().filter(o -> "foo".equals(o.someProperty)).count() == 1);
        assertTrue(testMessage.arbitraryObjectSet.stream().filter(o -> "should-not-be-replaced".equals(o.someProperty)).count() == 1);
    }
    
    /**
     * Ensure we support crawling a Map of objects.
     */
    @Test
    public void testArbitraryObjectMapProperty() {
        testMessage.arbitraryObjectMap = new HashMap<>();
        testMessage.arbitraryObjectMap.put("key1", new ArbitraryClass("@should-be-replaced"));
        testMessage.arbitraryObjectMap.put("key2", new ArbitraryClass("should-not-be-replaced"));
        
        interceptor.intercept("someApp", "someDevice", testMessage);
        assertEquals(2, testMessage.arbitraryObjectMap.size());
        assertEquals("foo", testMessage.arbitraryObjectMap.get("key1").someProperty);
        assertEquals("should-not-be-replaced", testMessage.arbitraryObjectMap.get("key2").someProperty);
    }
    
    /**
     * Ensure we support replacing of a string property directly on the top level
     * of a given object.
     */
    @Test
    public void testStringProperty() {
        testMessage.someMessageProperty = "@should-be-replaced";
        interceptor.intercept("someApp", "someDevice", testMessage);
        assertEquals("foo", testMessage.someMessageProperty);
        
        testMessage.someMessageProperty = "should-not-be-replaced";
        interceptor.intercept("someApp", "someDevice", testMessage);
        assertEquals("should-not-be-replaced", testMessage.someMessageProperty);
    }
    
    /**
     * Ensure we support replacing of a string property on a child property
     * of a given object.
     */
    @Test
    public void testArbitraryObjectProperty() {
        testMessage.arbitraryObject = new ArbitraryClass("@should-be-replaced");
        interceptor.intercept("someApp", "someDevice", testMessage);
        assertEquals("foo", testMessage.arbitraryObject.someProperty);
        
        testMessage.arbitraryObject = new ArbitraryClass("should-not-be-replaced");
        interceptor.intercept("someApp", "someDevice", testMessage);
        assertEquals("should-not-be-replaced", testMessage.arbitraryObject.someProperty);
    }
    
    /* *************************************************************************
     * Supporting classes
     * ********************************************************************** */
    
    public static class TestMessage extends Message {
        private static final long serialVersionUID = 1L;
        
        String someMessageProperty = null;
        ArbitraryClass arbitraryObject = null;
        String[] stringList = null;
        ArbitraryClass[] arbitraryObjectArray = null;
        List<ArbitraryClass> arbitraryObjectList = null;
        Map<String,ArbitraryClass> arbitraryObjectMap = null;
        Set<ArbitraryClass> arbitraryObjectSet = null;
        
    }
    
    public static class ArbitraryClass {
        String someProperty;
        ArbitraryClass(String property) {
            this.someProperty = property;
        }
    }
    
    public static class TestMessageCrawlerInterceptor extends AbstractMessagePropertyCrawlerInterceptor<TestMessage> {

        private IMessagePropertyStrategy<TestMessage> strategy;
        
        TestMessageCrawlerInterceptor(IMessagePropertyStrategy<TestMessage> strategy) {
            this.strategy = strategy;
        }
        
        @Override
        public List<IMessagePropertyStrategy<TestMessage>> getMessagePropertyStrategies() {
            return Arrays.asList(this.strategy);
        }

        @Override
        public void setMessagePropertyStrategies(List<IMessagePropertyStrategy<TestMessage>> strategies) {
        }
        
    }
    
}
