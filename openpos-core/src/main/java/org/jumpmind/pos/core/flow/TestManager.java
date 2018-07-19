package org.jumpmind.pos.core.flow;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(SCOPE_PROTOTYPE)
public class TestManager {

    
    IStateManager stateManager;

    protected void init(IStateManager stateManager) {
        this.stateManager = stateManager;
    }
    
    public void record() {
        
    }
}
