package org.jumpmind.pos.service;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

abstract public class AbstractServiceFactory {

    @Autowired
    protected ApplicationContext applicationContext;

    @Autowired
    protected EndpointInjector endpointInjector;

    @SuppressWarnings("unchecked")
    protected <T> T buildService(Class<T> serviceInterface) {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { serviceInterface },
                new ServiceEndpointBridgeInvocationHandler(applicationContext, endpointInjector));
    }

}
