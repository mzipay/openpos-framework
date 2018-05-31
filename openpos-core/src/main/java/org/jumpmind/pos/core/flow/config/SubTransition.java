package org.jumpmind.pos.core.flow.config;


public class SubTransition {

    private String returnActionName;
    private FlowConfig subFlowConfig;
    
    public SubTransition(String returnActionName, FlowConfig subFlowConfig) {
        super();
        this.returnActionName = returnActionName;
        this.subFlowConfig = subFlowConfig;
    }
    
    public String getReturnActionName() {
        return returnActionName;
    }
    public void setReturnActionName(String returnActionName) {
        this.returnActionName = returnActionName;
    }
    public FlowConfig getSubFlowConfig() {
        return subFlowConfig;
    }
    public void setSubFlowConfig(FlowConfig subFlowConfig) {
        this.subFlowConfig = subFlowConfig;
    }
    
}
