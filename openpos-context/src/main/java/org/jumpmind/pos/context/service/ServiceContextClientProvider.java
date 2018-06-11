package org.jumpmind.pos.context.service;

import org.jumpmind.pos.cache.service.CacheContainer;
import org.jumpmind.pos.cache.service.impl.ICache;
import org.jumpmind.pos.service.IServiceContextProvider;
import org.jumpmind.pos.service.InjectionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceContextClientProvider implements IServiceContextProvider {
    
    @Autowired
    private ContextService contextService;
    
    @Autowired
    private CacheContainer cacheContainer;

    @Override
    public Object resolveValue(String name, Class<?> type, InjectionContext injectionContext) {

        if (ContextServiceClient.class.equals(type)
                && !injectionContext.getArguments().isEmpty()) {
            
            // TODO should do a better inspection job to get deviceId parameter.
            String deviceId = (String) injectionContext.getArguments().get(0);
            
            ContextServiceClient client = new ContextServiceClient(contextService, deviceId);
            return client;
        }
        
        if (ICache.class.isAssignableFrom(type)) {
            return cacheContainer.getOrCreateCache(name);
        }
        
        return null;
    }

}
