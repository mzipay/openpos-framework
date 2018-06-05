package org.jumpmind.pos.service;

import java.lang.reflect.Method;

public class EndpointDefinition {
    
    private EndpointDefinition baseEndpointDefition;

    private String requestPath;
    private Class<?> endpointClass;
    private Method endpointMethod;
    private boolean isOverride;
    
    public String getRequestPath() {
        return requestPath;
    }
    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }
    public Class<?> getEndpointClass() {
        return endpointClass;
    }
    public void setEndpointClass(Class<?> endpointClass) {
        this.endpointClass = endpointClass;
    }
    public Method getEndpointMethod() {
        return endpointMethod;
    }
    public void setEndpointMethod(Method endpointMethod) {
        this.endpointMethod = endpointMethod;
    }
    public boolean isOverride() {
        return isOverride;
    }
    public void setOverride(boolean isOverride) {
        this.isOverride = isOverride;
    }
    public EndpointDefinition getBaseEndpointDefition() {
        return baseEndpointDefition;
    }
    public void setBaseEndpointDefition(EndpointDefinition baseEndpointDefition) {
        this.baseEndpointDefition = baseEndpointDefition;
    }
    

    
}
