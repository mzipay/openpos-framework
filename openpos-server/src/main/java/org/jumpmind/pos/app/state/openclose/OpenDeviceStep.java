package org.jumpmind.pos.app.state.openclose;

import org.jumpmind.pos.core.flow.ITransitionStep;
import org.jumpmind.pos.core.flow.Transition;
import org.jumpmind.pos.ops.service.OpsService;
import org.springframework.beans.factory.annotation.Autowired;

public class OpenDeviceStep implements ITransitionStep {

    @Autowired
    OpsService opsService;
    
    @Override
    public boolean isApplicable(Transition transition) {
        return false;
    }

    @Override
    public void arrive(Transition transition) {
    }

}
