package org.jumpmind.pos.core.flow;

import org.jumpmind.pos.core.flow.config.FlowConfig;

public class StateContext {

    private FlowConfig flowConfig;
    private Action action;
    private IState state;
    
    public StateContext() {
        
    }
    
    public StateContext(FlowConfig flowConfig, Action action, IState state) {
        super();
        this.flowConfig = flowConfig;
        this.action = action;
        this.state = state;
    }
    public FlowConfig getFlowConfig() {
        return flowConfig;
    }

    public void setFlowConfig(FlowConfig flowConfig) {
        this.flowConfig = flowConfig;
    }
    
    public Action getAction() {
        return action;
    }
    public void setAction(Action action) {
        this.action = action;
    }
    public IState getState() {
        return state;
    }
    public void setState(IState state) {
        this.state = state;
    }
    
    @Override
    public String toString() {
        return state != null ? state.toString() : super.toString();
    }
    
    
}
