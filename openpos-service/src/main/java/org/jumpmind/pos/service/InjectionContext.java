package org.jumpmind.pos.service;

import java.util.List;

public class InjectionContext {

    private EndpointDefinition endpointDefinition;
    private List<Object> arguments;
    
    public InjectionContext(EndpointDefinition endpointDefinition, List<Object> arguments) {
        super();
        this.endpointDefinition = endpointDefinition;
        this.arguments = arguments;
    }
    
    public EndpointDefinition getEndpointDefinition() {
        return endpointDefinition;
    }
    public void setEndpointDefinition(EndpointDefinition endpointDefinition) {
        this.endpointDefinition = endpointDefinition;
    }
    public List<Object> getArguments() {
        return arguments;
    }
    public void setArguments(List<Object> arguments) {
        this.arguments = arguments;
    }
    


    
}
