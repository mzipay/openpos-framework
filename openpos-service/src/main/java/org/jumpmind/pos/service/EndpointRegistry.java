package org.jumpmind.pos.service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

@Component
public class EndpointRegistry {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ConfigurableApplicationContext applicationContext;
        
    private Map<String, EndpointDefinition> baseEndpoints = new HashMap<>();
    private Map<String, EndpointDefinition> overrideEndpoints = new HashMap<>();
    
    @EventListener(ContextRefreshedEvent.class)
    public void initEndpoints() {
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            checkAndRegisterEndpoint(beanName);
        }
    }

    protected void checkAndRegisterEndpoint(String beanName) {
        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
        Class<?> beanClass = beanFactory.getType(beanName);
        
        for (Method method : beanClass.getDeclaredMethods()) {
            Endpoint baseEndpoint = AnnotationUtils.findAnnotation(method, Endpoint.class);
            EndpointOverride overrideEndpoint = AnnotationUtils.findAnnotation(method, EndpointOverride.class);
            
            if (baseEndpoint != null) {
                Object bean = applicationContext.getBean(beanName);
                if (bean != null) {
                    baseEndpoints.put(baseEndpoint.value(), buildEndpointDefinition(baseEndpoint.value(), method, bean));
                }
            } else if (overrideEndpoint != null) {
                Object bean = applicationContext.getBean(beanName);
                if (bean != null) {
                    overrideEndpoints.put(overrideEndpoint.value(), buildEndpointDefinition(overrideEndpoint.value(), method, bean));
                }
            }
        }
    }

    public EndpointDefinition findOverrideEndpointDefinition(String requestPath) {
        return overrideEndpoints.get(requestPath);
    }

    public EndpointDefinition findBaseEndpointDefinition(String requestPath) {
        return baseEndpoints.get(requestPath);
    }
    
    protected EndpointDefinition buildEndpointDefinition(String requestPath, Method method, Object bean) {
        EndpointDefinition endpointDefinition = new EndpointDefinition();
        endpointDefinition.setEndpointClass(bean.getClass());
        endpointDefinition.setEndpointMethod(method);
        endpointDefinition.setRequestPath(requestPath);
        endpointDefinition.setOverride(method.getAnnotation(EndpointOverride.class) != null);
        return endpointDefinition;
    }    
}
