package org.jumpmind.pos.app.state;

import org.jumpmind.pos.core.flow.Action;

public class CommitTransState extends AbstractState {
    
    @Override
    public void arrive(Action action) {
        // TODO save trans to DB
        // TODO print receipts.
        stateManager.endConversation();
    }    

    @Override
    protected String getDefaultBundleName() {
        return null;
    }

}
