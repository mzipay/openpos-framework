package org.jumpmind.pos.app.state;

import org.jumpmind.pos.app.demo.DemoException;
import org.jumpmind.pos.app.demo.DemoTransaction;
import org.jumpmind.pos.app.demo.DemoTransactionService;
import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.flow.In;
import org.jumpmind.pos.core.flow.ScopeType;
import org.springframework.beans.factory.annotation.Autowired;

public class TenderCashState extends AbstractState {
    
    @Autowired
    DemoTransactionService demoTransactionService;    
    
    @In(scope=ScopeType.Conversation)
    DemoTransaction currentTransaction;    
    
    @Override
    public void arrive(Action action) {
        super.arrive(action);
        demoTransactionService.applyTender(currentTransaction, "Cash",  action.getData());
        stateManager.doAction("CheckTransBalance");
    }

    @Override
    protected String getDefaultBundleName() {
        return null;
    }

}
