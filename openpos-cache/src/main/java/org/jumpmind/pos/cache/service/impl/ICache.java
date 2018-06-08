package org.jumpmind.pos.cache.service.impl;

import java.io.Closeable;
import java.util.function.Function;

import org.jumpmind.pos.cache.service.CacheConfig;
import org.jumpmind.pos.cache.service.NullCacheElement;

public interface ICache extends Closeable {
    
    public void setName(String name);
    public String getName();
    public void setConfig(CacheConfig config);
    public CacheConfig getConfig();
    
    public <T> T getValue(String key);
    public void setValue(String key, Object value);
    public void remove(String key);
    public void clear();
    
    public void init();
    public void close();
    
    @SuppressWarnings("unchecked")
    public default <T> T getOrLoad(String key, Function<String, T> loadFunction) {
        T value = getValue(key);
        if (value == null) {            
            value  = loadFunction.apply(key);
            if (value == null) {
                value = (T)new NullCacheElement();
            }
            setValue(key, value);
        }
        return value;
    }
    
}
