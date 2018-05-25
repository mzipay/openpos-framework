package org.jumpmind.pos.app.state;

import org.jumpmind.pos.app.demo.DemoTransaction;
import org.jumpmind.pos.app.demo.DemoTransactionService;
import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.flow.ActionHandler;
import org.jumpmind.pos.core.flow.IStateManager;
import org.jumpmind.pos.core.flow.In;
import org.jumpmind.pos.core.flow.ScopeType;
import org.jumpmind.pos.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;

public class TenderMenuState extends AbstractState {
    
    @In(scope=ScopeType.Node)
    IStateManager stateManager;
    @In(scope=ScopeType.Session)
    private User currentUser;    
    
    @Autowired
    DemoTransactionService demoTransactionService;
    
    @In(scope=ScopeType.Conversation)
    DemoTransaction currentTransaction;
    
    @Override
    public void arrive(Action action) {
        stateManager.showScreen(demoTransactionService.buildTenderMenuScreen(currentTransaction));
    }
    
    @ActionHandler
    protected void onAnyAction(Action action) {
        stateManager.doAction(action);
    }

    @Override
    protected String getDefaultBundleName() {
        return null;
    }
}
