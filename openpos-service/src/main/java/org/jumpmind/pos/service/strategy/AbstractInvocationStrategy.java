package org.jumpmind.pos.service.strategy;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.jumpmind.pos.persist.DBSession;
import org.jumpmind.pos.service.EndpointInjector;
import org.jumpmind.pos.service.ServiceSpecificConfig;
import org.jumpmind.pos.service.instrumentation.ServiceSample;
import org.jumpmind.pos.util.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;

abstract public class AbstractInvocationStrategy {

    @Autowired
    protected ApplicationContext applicationContext;

    @Autowired
    protected EndpointInjector endpointInjector;
    
    protected String buildPath(Method method) {
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
    
//    public Object invoke(ServiceSpecificConfig config, Object proxy, Method method, Object[] args) throws Throwable {
//        ServiceSample sample = startSample(config, proxy, method, args);
//        Object result = null;
//        try {            
//            result = invokeImpl(config, proxy, method, args);
//            endSampleSuccess(sample, config, proxy, method, args, result);
//        } catch (Throwable ex) {
//            endSampleError(sample, config, proxy, method, args, result, ex);
//            throw ex;
//        }
//        
//        return result;
//        
//    }
//    
//    protected abstract Object invokeImpl(ServiceSpecificConfig config, Object proxy, Method method, Object[] args) throws Throwable;
}
