package org.jumpmind.pos.service;


public interface IServiceContextProvider {

    public Object resolveValue(String name, Class<?> type, InjectionContext injectionContext);
    
}
