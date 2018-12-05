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
        StringBuilder path = new StringBuilder();
        Class<?> methodClazz = method.getDeclaringClass();
        RequestMapping clazzMapping = methodClazz.getAnnotation(RequestMapping.class);
        if (clazzMapping != null) {
            path.append(clazzMapping.value()[0]);
        }
        RequestMapping methodMapping = method.getAnnotation(RequestMapping.class);
        if (methodMapping != null) {
            path.append(methodMapping.value()[0]);
        }
        return endpointDispatcher.dispatch(path.toString(), args);
    }

}
