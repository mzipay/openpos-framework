package org.jumpmind.pos.cache.service;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.jumpmind.pos.cache.CacheException;
import org.jumpmind.pos.cache.service.impl.ICache;
import org.jumpmind.pos.cache.service.impl.ICacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

@Component
public class CacheContainer {

    private static final Logger log = LoggerFactory.getLogger(CacheContainer.class);
    
    public static final String DEFAULT_CACHE_NAME = "default";

    private List<CacheConfigs> cacheConfigs;
    private Map<String, ICache> caches = new ConcurrentHashMap<>();
    
    @Autowired
    private List<ICacheManager> cacheManagers;

    @PostConstruct
    public void init() {
        cacheConfigs = loadCacheConfigs();
        initCaches();
    }

    public ICache getCache(String cacheName) {
        return caches.get(cacheName);
    }

    public ICache createNewCache(String cacheName, String basedOnName) {
        CacheConfig cacheConfigTemplate = getCacheConfig(basedOnName);
        if (cacheConfigTemplate == null) {
            throw new CacheException("Can't find existing cache config for basedOnName='" + basedOnName + "'");
        }
        
        CacheConfig cacheConfig = new CacheConfig();
        cacheConfig.setName(cacheName);
        cacheConfig.setType(cacheConfigTemplate.getType());
        cacheConfig.setConfig(cacheConfigTemplate.getConfig());
        
        ICache cache = createCache(cacheConfig);
        caches.put(cacheName, cache);
        return cache;
    }

    protected void initCaches() {
        clearExistingCaches();
        Set<String> cacheNames = getCacheNames();

        for (String cacheName : cacheNames) {
            ICache cache = initCache(cacheName);
            caches.put(cacheName, cache);
        }
    }

    protected ICache initCache(String cacheName) {
        CacheConfig cacheConfig = getCacheConfig(cacheName);
        return createCache(cacheConfig);
    }

    protected CacheConfig getCacheConfig(String cacheName) {        
        for (CacheConfigs cacheConfigList : cacheConfigs) {
            for (CacheConfig cacheConfig : cacheConfigList.getCaches()) {
                if (cacheConfig.getName().equals(cacheName)) {
                    return cacheConfig;
                }
            }
        }

        return null;
    }

    protected Set<String> getCacheNames() {
        Set<String> cacheNames = new LinkedHashSet<>();

        for (CacheConfigs cacheConfigList : cacheConfigs) {
            for (CacheConfig cacheConfig : cacheConfigList.getCaches()) {
                cacheNames.add(cacheConfig.getName());
            }
        }

        return cacheNames;
    }

    protected void clearExistingCaches() {
        for (ICache cache : caches.values()) {
            cache.clear();
        }
        caches.clear();
    }
    
    protected ICache createCache(CacheConfig cacheConfig) {
        try {
            for (ICacheManager cacheManager : cacheManagers) {
                if (cacheManager.canCreateCache(cacheConfig)) {
                    return cacheManager.createCache(cacheConfig);
                }
            }
            
            Class<?> cacheClass = Thread.currentThread().getContextClassLoader().loadClass(cacheConfig.getType());
            ICache cache = (ICache) cacheClass.newInstance();
            cache.setName(cacheConfig.getName());
            cache.setConfig(cacheConfig);
            cache.init();
            return cache;
        } catch (Exception ex) {
            throw new CacheException("Failed to create cache named '" + cacheConfig.getName() + "'", ex);
        }
    }
        

    public List<CacheConfigs> loadCacheConfigs() {
        List<CacheConfigs> cacheConfigs = new ArrayList<>();
        
        List<String> configNames = new ArrayList<>();
        configNames.add("test-openpos-caches.yaml");
        configNames.add("openpos-caches.yaml");
        
        for (String configName : configNames) {            
            try {
                Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(configName);
                if (urls != null) {
                    while (urls.hasMoreElements()) {
                        URL url = urls.nextElement();
                        log.info(String.format("Loading %s...", url.toString()));
                        InputStream queryYamlStream = url.openStream();
                        CacheConfigs cacheConfigsEntry = new Yaml(new Constructor(CacheConfigs.class)).load(queryYamlStream);
                        cacheConfigs.add(cacheConfigsEntry);
                    }
                    
                } else {
                    log.info("Could not locate openpos-caches.yaml on the classpath.");
                }
            } catch (Exception ex) {
                throw new CacheException("Failed to load openpos-caches.yaml", ex);
            }
        }


//        Collections.reverse(cacheConfigs); // so we read highest classpath values first.
        return cacheConfigs;
    }


}
