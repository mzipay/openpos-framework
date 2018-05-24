package org.jumpmind.pos.app.state;

import java.util.Random;

import org.jumpmind.pos.app.demo.DemoException;
import org.jumpmind.pos.app.demo.DemoTransaction;
import org.jumpmind.pos.app.demo.DemoTransactionService;
import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.flow.In;
import org.jumpmind.pos.core.flow.ScopeType;
import org.springframework.beans.factory.annotation.Autowired;

public class TenderGiftCardState extends AbstractState {
    
    @Autowired
    DemoTransactionService demoTransactionService;    
    
    @In(scope=ScopeType.Conversation)
    DemoTransaction currentTransaction;
    
    
    @Override
    public void arrive(Action action) {
        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
        }
        try {            
            demoTransactionService.applyTender(currentTransaction, "GiftCard",  action.getData());
            stateManager.doAction("CheckTransBalance");
        } catch (DemoException ex) {
            stateManager.getUI().notify(ex.getMessage(), "Back");
        }        
    }    

    @Override
    protected String getDefaultBundleName() {
        return null;
    }
}
