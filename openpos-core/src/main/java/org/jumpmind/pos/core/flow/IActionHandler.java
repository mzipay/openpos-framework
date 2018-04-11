package org.jumpmind.pos.core.flow;

@FunctionalInterface
public interface IActionHandler {

    void handleAction(Action action);
    
}
