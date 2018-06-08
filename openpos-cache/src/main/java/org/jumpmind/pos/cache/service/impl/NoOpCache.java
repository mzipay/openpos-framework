package org.jumpmind.pos.cache.service.impl;

import org.jumpmind.pos.cache.service.CacheConfig;

public class NoOpCache implements ICache {
    
    private String name;
    private CacheConfig cacheConfig;    

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
        this.cacheConfig = config;
    }

    @Override
    public CacheConfig getConfig() {
        return cacheConfig;
    }

    @Override
    public <T> T getValue(String key) {
        return null;
    }

    @Override
    public void setValue(String key, Object value) {
        // No-op.
    }

    @Override
    public void remove(String key) {
    }

    @Override
    public void clear() {
    }

    @Override
    public void init() {
    }

    @Override
    public void close() {
    }

}
