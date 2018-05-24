package org.jumpmind.pos.app.state;

import org.jumpmind.pos.app.demo.DemoException;
import org.jumpmind.pos.app.demo.DemoTransaction;
import org.jumpmind.pos.app.demo.DemoTransactionService;
import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.flow.ActionHandler;
import org.jumpmind.pos.core.flow.IState;
import org.jumpmind.pos.core.flow.IStateManager;
import org.jumpmind.pos.core.flow.In;
import org.jumpmind.pos.core.flow.Out;
import org.jumpmind.pos.core.flow.ScopeType;
import org.jumpmind.pos.core.screen.SellItemScreen;
import org.jumpmind.pos.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;

public class SellState implements IState {

    @In(scope=ScopeType.Node)
    IStateManager stateManager;
    
    @In(scope=ScopeType.Session)
    private User currentUser;    
    
    @Autowired
    DemoTransactionService demoTransactionService;
    
    @In(required=false, scope=ScopeType.Conversation)
    @Out(required=false, scope=ScopeType.Conversation)
    DemoTransaction currentTransaction;
    
    @Override
    public void arrive(Action action) {
        stateManager.showScreen(buildScreen());
    }

    protected SellItemScreen buildScreen() {
        if (currentTransaction == null) {
            currentTransaction = demoTransactionService.startTransaction(currentUser, stateManager.getNodeId());
        }
        
        return demoTransactionService.buildScreen(currentTransaction);
    }
    
    @ActionHandler
    protected void onCustomerSearchFinished(Action action) {
        stateManager.showScreen(buildScreen());
    }
    
    @ActionHandler
    protected void onItemLookupFailedAcknowledged(Action action) {
        stateManager.showScreen(buildScreen());
    }
    
    @ActionHandler
    protected void onNext(Action action) {
        String scanData = action.getData();
        try {            
            currentTransaction = demoTransactionService.ringItem(currentTransaction, scanData);
            stateManager.showScreen(buildScreen());
        } catch (DemoException ex) {
            stateManager.getUI().notify(ex.getMessage(), "ItemLookupFailedAcknowledged");
        }
    }
    
    @ActionHandler    
    protected void onCustomerSearchComplete(Action action) {
        stateManager.getUI().notify("Got the customer selected " + action.getData(), "CustomerSelectAcknowledged");
    }
    
    @ActionHandler
    protected void onCustomerSelectAcknowledged(Action action) {
        stateManager.showScreen(buildScreen());        
    }
}
