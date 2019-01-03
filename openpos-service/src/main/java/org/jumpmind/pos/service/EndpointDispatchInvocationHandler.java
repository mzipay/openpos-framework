package org.jumpmind.pos.service;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jumpmind.pos.util.web.ConfiguredRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Component
public class EndpointDispatchInvocationHandler implements InvocationHandler {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private EndpointInjector endpointInjector;

    @Autowired
    private ServiceConfig serviceConfig;

    @Value("${openpos.installationId}")
    String installationId;

    public EndpointDispatchInvocationHandler() {
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("equals")) {
            return false;
        }

        ServiceSpecificConfig config = getSpecificConfig(method);
        if (config.isLocal()) {
            return invokeLocal(proxy, method, args);
        } else {
            return invokeRemote(config, method, args);
        }

    }
    
    protected Object invokeRemote(ServiceSpecificConfig config, Method method, Object[] args) throws Throwable {
        String url = config.getUrl();
        int httpTimeoutInSecond = config.getHttpTimeout();
        ConfiguredRestTemplate template = new ConfiguredRestTemplate(httpTimeoutInSecond);
        String path = buildPath(method);
        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        RequestMethod[] requestMethods = mapping.method();
        if (requestMethods != null && requestMethods.length > 0) {
            HttpMethod requestMethod = translate(requestMethods[0]);
            url = String.format("%s%s", url, path);
            Object requestBody = findRequestBody(method, args);
            Object[] newArgs = findArgs(method, args);
            if (method.getReturnType().equals(Void.TYPE)) {
                template.execute(url, requestBody, requestMethod, newArgs);
            } else {
                return template.execute(url, requestBody, method.getReturnType(), requestMethod, newArgs);
            }
        } else {
            throw new IllegalStateException("A method must be specified on the @RequestMapping");
        }
        return null;
    }
   

    protected Object invokeLocal(Object proxy, Method method, Object[] args) throws Throwable {
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
                try {
                    return targetMethod.invoke(obj, args);
                } catch (InvocationTargetException e) {
                    throw e.getTargetException();
                }
            }
        }

        throw new PosServerException(String.format("No endpoint found for path '%s' Please define a Spring-discoverable @Componant class, "
                + "with a method annotated like  @Endpoint(\"%s\")", path, path));
    }
    
    private ServiceSpecificConfig getSpecificConfig(Method method) {
        Class<?> methodClazz = method.getDeclaringClass();
        RestController restController = methodClazz.getAnnotation(RestController.class);
        if (restController != null && isNotBlank(restController.value())) {
            return serviceConfig.getServiceConfig(installationId, restController.value());
        } else {
            throw new IllegalStateException(methodClazz.getSimpleName() + " must declare @" + RestController.class.getSimpleName()
                    + " and it must have the value() attribute set");
        }
    }
    
    private Object[] findArgs(Method method, Object[] args) {
        List<Object> newArgs = new ArrayList<>();
        Annotation[][] types = method.getParameterAnnotations();
        for (int i = 0; i < types.length; i++) {
            Annotation[] argAnnotations = types[i];
            for (Annotation annotation : argAnnotations) {
                if (PathVariable.class.equals(annotation.annotationType()) || RequestParam.class.equals(annotation.annotationType())) {
                    newArgs.add(args[i]);
                }
                
            }
        }
        return newArgs.toArray();
    }
    
    private Object findRequestBody(Method method, Object[] args) {
        Annotation[][] types = method.getParameterAnnotations();
        for (int i = 0; i < types.length; i++) {
            Annotation[] argAnnotations = types[i];
            for (Annotation annotation : argAnnotations) {
                if (RequestBody.class.equals(annotation.annotationType())) {
                    return args[i];
                }
                
            }
        }
           
        return null;
    }
    
    private HttpMethod translate(RequestMethod method) {
        return HttpMethod.valueOf(method.name());
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
            if (methodMapping.path() != null && methodMapping.path().length > 0) {
                path.append(methodMapping.path()[0]);
            } else if (methodMapping.value() != null && methodMapping.value().length > 0) {
                path.append(methodMapping.value()[0]);
            }
        }
        return path.toString();
    }

}
