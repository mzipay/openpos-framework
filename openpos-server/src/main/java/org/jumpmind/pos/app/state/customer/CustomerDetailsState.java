package org.jumpmind.pos.app.state.customer;

import org.jumpmind.pos.app.state.AbstractState;
import org.jumpmind.pos.core.flow.Action;

public class CustomerDetailsState extends AbstractState {

    @Override
    protected String getDefaultBundleName() {
        return null;
    }
    
    @Override
    public void arrive(Action action) {
    }

}
