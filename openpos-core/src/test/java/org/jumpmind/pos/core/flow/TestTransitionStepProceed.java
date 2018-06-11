package org.jumpmind.pos.core.flow;

import org.apache.commons.lang3.StringUtils;
import org.jumpmind.pos.core.service.DeviceService;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

public class TestTransitionStepProceed implements ITransitionStep {
    
    @In(scope = ScopeType.Node)
    protected IStateManager stateManager;    

    @Override
    public boolean isApplicable(Transition transition) {
        Assert.assertNotNull(stateManager);
        return StringUtils.equals(transition.getOriginalAction().getName(), "TestTransitionProceed");
    }

    @Override
    public void arrive(Transition transition) {
        Assert.assertNotNull(stateManager);
        transition.proceed();
    }

}
