package org.jumpmind.pos.service;

public class InjectionContext {

    private Object[] arguments;

    public InjectionContext(Object[] arguments) {
        super();
        this.arguments = arguments;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

}
