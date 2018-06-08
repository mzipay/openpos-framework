package org.jumpmind.pos.cache.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.jcs.engine.control.CompositeCache;
import org.apache.commons.jcs.engine.control.CompositeCacheManager;
import org.jumpmind.pos.cache.CacheException;
import org.jumpmind.pos.cache.service.CacheConfig;
import org.jumpmind.pos.cache.service.CacheConfigs;
import org.springframework.stereotype.Component;

@Component
public class JCSCacheManager implements ICacheManager {

    public List<ICache> initCaches(List<CacheConfigs> cacheConfigsList) {
        List<ICache> caches = new ArrayList<>();
        for (CacheConfigs cacheConfigs : cacheConfigsList) {
            for (CacheConfig cacheConfig : cacheConfigs.getCaches()) {
                if (canCreateCache(cacheConfig)) {
                    caches.add(createCache(cacheConfig));
                }
            }
        }

        return caches;
    }

    public boolean canCreateCache(CacheConfig cacheConfig) {
        return (cacheConfig.getType().equals(JCSCache.class.getName()));
    }

    @Override
    public ICache createCache(CacheConfig cacheConfig) {
        if (!canCreateCache(cacheConfig)) {
            throw new CacheException("Cache manager " + this + 
                    " is not able to create a cache of type " + cacheConfig);
        }

        Properties props = new Properties();
        for (String propName : cacheConfig.getConfig().keySet()) {
            props.put(propName, cacheConfig.getConfig().get(propName));
        }

        CompositeCacheManager jcsCacheManaged = CompositeCacheManager.getUnconfiguredInstance();
        jcsCacheManaged.configure(props);
        CompositeCache<String, Object> jcsCache = jcsCacheManaged.getCache(cacheConfig.getName());
        ICache cache = new JCSCache(jcsCache);
        cache.setName(cacheConfig.getName());
        cache.setConfig(cacheConfig);
        return cache;            
    }
}
