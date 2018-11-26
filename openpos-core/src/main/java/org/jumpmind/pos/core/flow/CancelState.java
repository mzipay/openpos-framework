package org.jumpmind.pos.core.flow;

import org.jumpmind.pos.server.model.Action;

public class CancelState implements IState {

    @Override
    public void arrive(Action action) {
        // No op.
    }

}
