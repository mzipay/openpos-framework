package org.jumpmind.pos.app.state.customer;

import org.jumpmind.pos.app.state.AbstractState;
import org.jumpmind.pos.core.flow.Action;

public class CustomerSearchResultState extends AbstractState {
    
    @Override
    public void arrive(Action action) {
        super.arrive(action);
    }

    @Override
    protected String getDefaultBundleName() {
        return null;
    }

}
