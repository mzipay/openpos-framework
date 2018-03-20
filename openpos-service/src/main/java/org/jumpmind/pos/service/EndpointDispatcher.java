package org.jumpmind.pos.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EndpointDispatcher {

    @Autowired
    private EnpointRegistry endpointRegistry;

    @SuppressWarnings("unchecked")
    public <T> T dispatch(String path, Object... args) {
        EndpointDefinition baseEndpointDefinition = endpointRegistry.findBaseEndpointDefinition(path);
        EndpointDefinition overrideEndpointDefinition = endpointRegistry.findOverrideEndpointDefinition(path);
        
        Object result = null;
        
        if (overrideEndpointDefinition != null) {
            result = invokeOverrideEndpoint(baseEndpointDefinition, overrideEndpointDefinition, args);
        } else if (baseEndpointDefinition != null) {
            result = invokeBaseEndpoint(baseEndpointDefinition, args);
        } else {
            throw noEndpointFound(path);
        }
        
        return (T)result;
    }

    protected Object invokeOverrideEndpoint(EndpointDefinition baseEndpointDefinition, 
            EndpointDefinition overrideEndpointDefinition, Object...args) {
        
        Method method = overrideEndpointDefinition.getEndpointMethod();
        
        List<Object> parameters = new ArrayList<>();
        
        int index = 0;
        for (Class<?> expectedType : method.getParameterTypes()) {
            if (expectedType.isAssignableFrom(baseEndpointDefinition.getEndpointInstance().getClass())) {
                parameters.add(baseEndpointDefinition.getEndpointInstance());
                index--;
            } else if (index < args.length) {
                parameters.add(args[index]);
            }
            
            index++;
        }
        
        Object result;
        try {
            result = method.invoke(overrideEndpointDefinition.getEndpointInstance(), parameters.toArray());
        } catch (Exception ex) {
            throw new PosServerException(String.format("Failed to invoke override endpoint method %s", method), ex);
        }
        
        return result;
    }

    
    protected Object invokeBaseEndpoint(EndpointDefinition baseEndpointDefinition, Object...args) {
        Method method = baseEndpointDefinition.getEndpointMethod();
        
        List<Object> parameters = new ArrayList<>();
        
        int index = 0;
        for (Class<?> expectedType : method.getParameterTypes()) {
            if (expectedType.isAssignableFrom(baseEndpointDefinition.getEndpointInstance().getClass())) {
                parameters.add(baseEndpointDefinition.getEndpointInstance());
                index--;
            } else if (index < args.length) {
                parameters.add(args[index]);
            }
            
            index++;
        }
        
        Object result;
        try {
            result = method.invoke(baseEndpointDefinition.getEndpointInstance(), parameters);
        } catch (Exception ex) {
            throw new PosServerException(String.format("Failed to invoke override endpoint method %s", method), ex);
        }
        
        return result;
    }

    protected RuntimeException noEndpointFound(String path) {
        throw new PosServerException(String.format("No endpoint found for path '%s' Please define a Spring-discoverable @Componant class, "
                + "with a method annotated like  @Endpoint(\"%s\")", path, path));
    }
    
}
