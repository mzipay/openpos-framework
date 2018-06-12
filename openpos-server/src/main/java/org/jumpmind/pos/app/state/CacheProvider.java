package org.jumpmind.pos.app.state;

import java.lang.reflect.Field;

import org.jumpmind.pos.cache.service.CacheContainer;
import org.jumpmind.pos.cache.service.impl.ICache;
import org.jumpmind.pos.core.flow.IScopeValueProvider;
import org.jumpmind.pos.core.flow.ScopeType;
import org.jumpmind.pos.core.flow.ScopeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CacheProvider implements IScopeValueProvider {
    
    @Autowired
    private CacheContainer cacheContainer;

    @Override
    public ScopeValue getValue(String name, ScopeType scope, Object target, Field field) {
        if (field.getType().isAssignableFrom(ICache.class)) {
            ICache cache = cacheContainer.getOrCreateCache(name);
            return new ScopeValue(cache);
        }
        
        return null;
    }

}
