package org.jumpmind.pos.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class EndpointDispatcher {

    @Autowired
    private EndpointRegistry endpointRegistry;
    @Autowired
    private EndpointInjector endpointInjector;
    @Autowired
    ApplicationContext applicationContext;

    @SuppressWarnings("unchecked")
    public <T> T dispatch(String path, Object... args) {
        EndpointDefinition baseEndpointDefinition = endpointRegistry.findBaseEndpointDefinition(path);
        EndpointDefinition overrideEndpointDefinition = endpointRegistry.findOverrideEndpointDefinition(path);
        
        Object result = null;
        
        if (overrideEndpointDefinition != null) {
            overrideEndpointDefinition.setBaseEndpointDefition(baseEndpointDefinition);
            result = invokeEndpoint(overrideEndpointDefinition, args);
        } else if (baseEndpointDefinition != null) {
            result = invokeEndpoint(baseEndpointDefinition, args);
        } else {
            throw noEndpointFound(path);
        }
        
        return (T)result;
    }
    
    protected Object invokeEndpoint(
            EndpointDefinition endpointDefinition,
            Object...args) {
        
        Method method = endpointDefinition.getEndpointMethod();
        
        List<Object> parameters = new ArrayList<>();
        
        Object baseEndpointInstance = endpointDefinition.getBaseEndpointDefition() != null ? 
                applicationContext.getBean(endpointDefinition.getBaseEndpointDefition().getEndpointClass()) : null;
        Object endpointInstance = applicationContext.getBean(endpointDefinition.getEndpointClass());

        int index = 0;
        for (Class<?> expectedType : method.getParameterTypes()) {
            if (baseEndpointInstance != null 
                    && expectedType.isAssignableFrom(baseEndpointInstance.getClass())) {
                parameters.add(baseEndpointInstance);
                index--;
            } else if (index < args.length) {
                parameters.add(args[index]);
            }
            
            index++;
        }
        
        InjectionContext injectionConext = new InjectionContext(endpointDefinition, parameters);
        
        endpointInjector.performInjections(endpointInstance, injectionConext);        
        
        Object result;
        try {
            result = method.invoke(endpointInstance, parameters.toArray());
        } catch (Exception ex) {
            throw failedToInvokeEndpoint(endpointDefinition, parameters, ex);
        }
        
        return result;
    }

    protected void createContextClient(String deviceId) {
        
    }

    protected RuntimeException failedToInvokeEndpoint(EndpointDefinition baseEndpointDefinition, List<Object> parameters, Exception cause) {
        Method method = baseEndpointDefinition.getEndpointMethod();
        String msg = String.format("Failed to invoke endpoint method \"%s\".\nExected argument types %s\nActual parameters %s",
                method, Arrays.asList(method.getParameterTypes()), parameters);
        throw new PosServerException(msg, cause);
    }

    protected RuntimeException noEndpointFound(String path) {
        throw new PosServerException(String.format("No endpoint found for path '%s' Please define a Spring-discoverable @Componant class, "
                + "with a method annotated like  @Endpoint(\"%s\")", path, path));
    }
}
