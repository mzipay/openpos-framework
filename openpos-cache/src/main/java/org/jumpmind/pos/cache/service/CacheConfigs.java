package org.jumpmind.pos.cache.service;

import java.util.ArrayList;
import java.util.List;

public class CacheConfigs {

    List<CacheConfig> caches = new ArrayList<>();

    public List<CacheConfig> getCaches() {
        return caches;
    }

    public void setCaches(List<CacheConfig> caches) {
        this.caches = caches;
    }

    
    
}
