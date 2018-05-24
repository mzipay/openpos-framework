package org.jumpmind.pos.app.state;

import org.jumpmind.pos.app.demo.DemoTransaction;
import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.flow.In;
import org.jumpmind.pos.core.flow.ScopeType;

public class CheckTransBalanceState extends AbstractState {
    
    @In(scope=ScopeType.Conversation)
    DemoTransaction currentTransaction;
    
    @Override
    public void arrive(Action action) {
        super.arrive(action);
        
        if (currentTransaction.getBalanceDue() != null 
                && currentTransaction.getBalanceDue().equals("0.00")) { // demo only! 
            stateManager.doAction("CommitTransaction");
        } else {
            stateManager.doAction("ReturnToTenderMenu");
        }
        
    }

    @Override
    protected String getDefaultBundleName() {
        return null;
    }

}
