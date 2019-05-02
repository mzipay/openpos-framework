package org.jumpmind.pos.core.flow;

import java.util.Arrays;

import org.jumpmind.pos.core.flow.config.FlowConfig;
import org.jumpmind.pos.core.flow.config.YamlConfigProvider;

public class StateManagerTestUtils {
    
    
    protected static StateManager buildStateManager(Injector injector, String yamlConfigPath) throws Exception {
        
        YamlConfigProvider provider = new YamlConfigProvider();
        provider.load("pos", Thread.currentThread().getContextClassLoader().getResourceAsStream(yamlConfigPath));
        FlowConfig flowConfig = provider.getConfigByName("pos", "100-1", "TestDoubleSubstateExitFlow");
        
        StateManager stateManager = new StateManager();
       
        TestUtil.setField(stateManager, "actionHandler", new ActionHandlerImpl());
        TestUtil.setField(stateManager, "injector", injector);
        TestUtil.setField(stateManager, "outjector", new Outjector());
        TestUtil.setField(stateManager, "transitionSteps", Arrays.asList(new TestTransitionStepCancel(), new TestTransitionStepProceed()));
        TestUtil.setField(stateManager, "stateLifecyce", new StateLifecycle());
        
        stateManager.setInitialFlowConfig(flowConfig);
        
        stateManager.init("pos", "100-1");
        
        return stateManager;
    }

}
