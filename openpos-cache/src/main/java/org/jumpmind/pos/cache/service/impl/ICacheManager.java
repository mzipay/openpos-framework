package org.jumpmind.pos.cache.service.impl;

import java.util.List;

import org.jumpmind.pos.cache.service.CacheConfig;
import org.jumpmind.pos.cache.service.CacheConfigs;

public interface ICacheManager {
    
    public List<ICache> initCaches(List<CacheConfigs> cacheConfigs);
    
    public boolean canCreateCache(CacheConfig cacheConfig);
    
    public ICache createCache(CacheConfig cacheConfig);
}
