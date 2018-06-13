package org.jumpmind.pos.cache.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.jumpmind.pos.cache.service.impl.ICache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class CacheContainerTest {
    
    @Autowired
    private CacheContainer cacheContainer;
 
    @Test
    public void testLoadCacheConfig() {
        List<CacheConfigs> cacheConfigs = cacheContainer.loadCacheConfigs();
        assertEquals(2, cacheConfigs.size());
        assertEquals("default", cacheConfigs.get(0).getCaches().get(0).getName());
        assertEquals("org.jumpmind.pos.cache.service.impl.JCSCache", cacheConfigs.get(0).getCaches().get(0).getType());
        assertEquals("20000", cacheConfigs.get(0).getCaches().get(0).getConfig().get("jcs.default.cacheattributes.MaxObjects"));
        assertEquals("3600", cacheConfigs.get(0).getCaches().get(0).getConfig().get("jcs.default.cacheattributes.MaxMemoryIdleTimeSeconds"));
    }
    
    @Test 
    public void testDynamicCacheConfig() {
        ICache cache = cacheContainer.getCache("myCache");
        if (cache == null) {
            cache = cacheContainer.createNewCache("myCache", CacheContainer.DEFAULT_CACHE_NAME);
        }
        
        assertEquals(cache.getName(), "myCache");
        assertEquals("myCache", cache.getConfig().getName());
        assertEquals("org.jumpmind.pos.cache.service.impl.JCSCache", cache.getConfig().getType());
        assertEquals("20000", cache.getConfig().getConfig().get("jcs.default.cacheattributes.MaxObjects"));
        assertEquals("3600", cache.getConfig().getConfig().get("jcs.default.cacheattributes.MaxMemoryIdleTimeSeconds"));        
    }
    
    @Test
    
    public void testCacheSimpleObject() {
        ICache cache = cacheContainer.getCache("simpleMap");
        if (cache == null) {
            cache = cacheContainer.createNewCache("simpleMap", CacheContainer.DEFAULT_CACHE_NAME);
        }
        assertEquals(cache.getName(), "simpleMap");
        
        final String KEY = "simpleObjectTest1";
        final String KEY2 = "simpleObjectTest2";
        
        assertNull(cache.getValue(KEY));
        assertNull(cache.getValue(KEY2));
        
        SimpleObject o = new SimpleObject("someValue");
        
        cache.setValue(KEY, o);
        assertEquals(o, cache.getValue(KEY));
        assertNull(cache.getValue(KEY2));
                
        SimpleObject o2 = new SimpleObject("someOtherValue");
        cache.setValue(KEY2, o2);
        assertEquals(o, cache.getValue(KEY));
        assertEquals(o2, cache.getValue(KEY2));
        
        cache.remove(KEY2);
        assertEquals(o, cache.getValue(KEY));
        assertNull(cache.getValue(KEY2));
        
        cache.remove(KEY);
        assertNull(cache.getValue(KEY));
        assertNull(cache.getValue(KEY2));        
        
        cache.setValue(KEY, o);
        cache.setValue(KEY2, o2);
        assertEquals(o, cache.getValue(KEY));
        assertEquals(o2, cache.getValue(KEY2));
        
        cache.clear();
        assertNull(cache.getValue(KEY));
        assertNull(cache.getValue(KEY2));        
    }    

    @Test 
    public void testNoOpCache() {
        ICache cache = cacheContainer.getCache("noOpCache");
        if (cache == null) {
            cache = cacheContainer.createNewCache("noOpCache", CacheContainer.DEFAULT_CACHE_NAME);
        }
        assertEquals(cache.getName(), "noOpCache");
        
        final String KEY = "simpleObjectTest1";
        final String KEY2 = "simpleObjectTest2";
        
        assertNull(cache.getValue(KEY));
        assertNull(cache.getValue(KEY2));
        
        SimpleObject o = new SimpleObject("someValue");
        SimpleObject o2 = new SimpleObject("someOtherValue");
        
        cache.setValue(KEY, o);
        cache.setValue(KEY2, o2);
        assertNull(cache.getValue(KEY));
        assertNull(cache.getValue(KEY2));
        
        cache.remove(KEY2);
        assertNull(cache.getValue(KEY));
        assertNull(cache.getValue(KEY2));
        
        cache.remove(KEY);
        assertNull(cache.getValue(KEY));
        assertNull(cache.getValue(KEY2));        
        
        cache.setValue(KEY, o);
        cache.setValue(KEY2, o2);
        assertNull(cache.getValue(KEY));
        assertNull(cache.getValue(KEY2));
        
        cache.clear();
        assertNull(cache.getValue(KEY));
        assertNull(cache.getValue(KEY2));        
    } 
    
    @Test 
    public void testJCSCacheSimpleTest() {
        ICache cache = cacheContainer.getCache(CacheContainer.DEFAULT_CACHE_NAME);
        
        assertEquals(cache.getName(), "default");
        
        final String KEY = "simpleObjectTest1";
        final String KEY2 = "simpleObjectTest2";
        
        assertNull(cache.getValue(KEY));
        assertNull(cache.getValue(KEY2));
        
        SimpleObject o = new SimpleObject("someValue");
        
        cache.setValue(KEY, o);
        assertEquals(o, cache.getValue(KEY));
        assertNull(cache.getValue(KEY2));
                
        SimpleObject o2 = new SimpleObject("someOtherValue");
        cache.setValue(KEY2, o2);
        assertEquals(o, cache.getValue(KEY));
        assertEquals(o2, cache.getValue(KEY2));
        
        cache.remove(KEY2);
        assertEquals(o, cache.getValue(KEY));
        assertNull(cache.getValue(KEY2));
        
        cache.remove(KEY);
        assertNull(cache.getValue(KEY));
        assertNull(cache.getValue(KEY2));        
        
        cache.setValue(KEY, o);
        cache.setValue(KEY2, o2);
        assertEquals(o, cache.getValue(KEY));
        assertEquals(o2, cache.getValue(KEY2));
        
        cache.clear();
        assertNull(cache.getValue(KEY));
        assertNull(cache.getValue(KEY2));        
    }
    
    @Test 
    public void testMultipleJCSCaches() {
        ICache jcs1 = cacheContainer.getCache("testFirstJCS");        
        ICache jcs2 = cacheContainer.getCache("testSecondJCS");

        assertEquals("testFirstJCS", jcs1.getConfig().getName());
        assertEquals("org.jumpmind.pos.cache.service.impl.JCSCache", jcs1.getConfig().getType());
        assertEquals("testSecondJCS", jcs2.getConfig().getName());
        assertEquals("org.jumpmind.pos.cache.service.impl.JCSCache", jcs2.getConfig().getType());
        
        final String KEY = "simpleObjectTest1";
        final String KEY2 = "simpleObjectTest2";
        SimpleObject o = new SimpleObject("someValue");
        SimpleObject o2 = new SimpleObject("someOtherValue");
        
        jcs1.setValue(KEY, o);
        assertEquals(o, jcs1.getValue(KEY));
        assertNull(jcs1.getValue(KEY2));
        assertNull(jcs2.getValue(KEY));
        assertNull(jcs2.getValue(KEY2));
        
        jcs2.setValue(KEY, o);
        assertEquals(o, jcs1.getValue(KEY));
        assertNull(jcs1.getValue(KEY2));
        assertEquals(o, jcs2.getValue(KEY));
        assertNull(jcs2.getValue(KEY2));        
        
        jcs1.setValue(KEY2, o);
        assertEquals(o, jcs1.getValue(KEY));
        assertEquals(o, jcs1.getValue(KEY2));
        assertEquals(o, jcs2.getValue(KEY));
        assertNull(jcs2.getValue(KEY2));
        
        jcs2.setValue(KEY2, o);
        assertEquals(o, jcs1.getValue(KEY));
        assertEquals(o, jcs1.getValue(KEY2));
        assertEquals(o, jcs2.getValue(KEY));
        assertEquals(o, jcs2.getValue(KEY2));        
        
        jcs1.clear();
        assertNull(jcs1.getValue(KEY));
        assertNull(jcs1.getValue(KEY2));
        assertEquals(o, jcs2.getValue(KEY));
        assertEquals(o, jcs2.getValue(KEY2));
        
        jcs2.clear();
        assertNull(jcs1.getValue(KEY));
        assertNull(jcs1.getValue(KEY2));
        assertNull(jcs2.getValue(KEY));
        assertNull(jcs2.getValue(KEY2));        
    }
    
    @Test 
    public void testGetOrLoad() {
        ICache cache = cacheContainer.getCache(CacheContainer.DEFAULT_CACHE_NAME);
        final String KEY = "oneSimpleObject";
        assertNull(cache.getValue(KEY));
        SimpleObject simpleObject = cache.getOrLoad(KEY, k -> {
            return new SimpleObject(k);
        });
        
        assertEquals(new SimpleObject(KEY), simpleObject);
    }
}
