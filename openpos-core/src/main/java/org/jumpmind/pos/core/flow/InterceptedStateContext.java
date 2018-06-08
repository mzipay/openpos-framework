package org.jumpmind.pos.core.flow;

import java.util.HashMap;
import java.util.Map;

public class InterceptedStateContext extends StateContext {
    
    private Map<Class<? extends IState>, IState> terminatingStateToNextStateMapping = new HashMap<>();

    public void mapTerminatingState(Class<? extends IState> terminingState, IState proceedsToState) {
        terminatingStateToNextStateMapping.put(terminingState, proceedsToState);
    }

    public IState getProceedToStateForTerminatingState(Class<? extends IState> targetStateClass) {
        return terminatingStateToNextStateMapping.get(targetStateClass);
    }
    
}
