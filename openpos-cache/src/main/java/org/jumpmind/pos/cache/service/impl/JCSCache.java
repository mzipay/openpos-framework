package org.jumpmind.pos.cache.service.impl;

import java.util.function.Function;

import org.apache.commons.jcs.access.CacheAccess;
import org.apache.commons.jcs.engine.control.CompositeCache;
import org.jumpmind.pos.cache.service.CacheConfig;
import org.jumpmind.pos.cache.service.NullCacheElement;

public class JCSCache implements ICache {
    
    private String name;
    private CacheConfig config;
    private CacheAccess<String , Object> cache;
    
    public JCSCache(CompositeCache<String , Object> cache) {
        this.cache = new CacheAccess<>(cache);
    }
    
    @Override
    public void init() {
        
    }    

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setConfig(CacheConfig config) {
        this.config = config;
    }

    @Override
    public CacheConfig getConfig() {
        return config;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getValue(String key) {
        return (T) cache.get(key);
    }

    @Override
    public void setValue(String key, Object value) {
        cache.put(key, value);
    }

    @Override
    public void remove(String key) {
        cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }
    
    public void close() {
    }



}
