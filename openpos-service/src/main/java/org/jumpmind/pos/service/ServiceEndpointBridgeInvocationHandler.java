package org.jumpmind.pos.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;

import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;

public class ServiceEndpointBridgeInvocationHandler implements InvocationHandler {

    private ApplicationContext applicationContext;

    private EndpointInjector endpointInjector;

    public ServiceEndpointBridgeInvocationHandler(ApplicationContext applicationContext, EndpointInjector endpointInjector) {
        this.applicationContext = applicationContext;
        this.endpointInjector = endpointInjector;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String path = buildPath(method);
        Object obj = applicationContext.getBean(path);
        Collection<Object> beans = applicationContext.getBeansWithAnnotation(EndpointOverride.class).values();
        for (Object testObj : beans) {
            EndpointOverride override = testObj.getClass().getAnnotation(EndpointOverride.class);
            if (override.path().equals(path)) {
                obj = testObj;
            }
        }

        if (obj != null) {
            endpointInjector.performInjections(obj, new InjectionContext(args));
            Method targetMethod = obj.getClass().getMethod(method.getName(), method.getParameterTypes());
            if (targetMethod != null) {
                return targetMethod.invoke(obj, args);
            }
        }

        throw new PosServerException(String.format("No endpoint found for path '%s' Please define a Spring-discoverable @Componant class, "
                + "with a method annotated like  @Endpoint(\"%s\")", path, path));

    }

    private String buildPath(Method method) {
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
        return path.toString();
    }

}
