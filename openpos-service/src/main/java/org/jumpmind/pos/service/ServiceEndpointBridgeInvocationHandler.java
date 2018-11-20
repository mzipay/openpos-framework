package org.jumpmind.pos.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.springframework.web.bind.annotation.RequestMapping;

public class ServiceEndpointBridgeInvocationHandler implements InvocationHandler {

    private EndpointDispatcher endpointDispatcher;
    
    public ServiceEndpointBridgeInvocationHandler(EndpointDispatcher endpointDispatcher) {
        this.endpointDispatcher = endpointDispatcher;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            return endpointDispatcher.dispatch(mapping.value()[0], args);
    }

}
