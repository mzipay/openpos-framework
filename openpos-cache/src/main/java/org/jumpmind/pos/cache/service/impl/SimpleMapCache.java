package org.jumpmind.pos.cache.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jumpmind.pos.cache.service.CacheConfig;

public class SimpleMapCache implements ICache {
    
    private String name;
    private CacheConfig cacheConfig;
    private Map<String, Object> cache = new ConcurrentHashMap<>();
    
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
    
    public void setConfig(Map<String, String> config) {
        // no op for now.
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

    @Override
    public void setConfig(CacheConfig cacheConfig) {
        this.cacheConfig = cacheConfig;
    }

    @Override
    public void init() {
    }

    @Override
    public CacheConfig getConfig() {
        return cacheConfig;
    }

    @Override
    public void close() {
    }

}
