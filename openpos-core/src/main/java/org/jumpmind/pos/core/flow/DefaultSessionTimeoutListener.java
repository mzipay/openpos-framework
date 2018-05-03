package org.jumpmind.pos.core.flow;

import org.springframework.stereotype.Component;

@Component
public class DefaultSessionTimeoutListener implements ISessionTimeoutListener {

    @Override
    public void onSessionTimeout(StateManager stateManager) {
        stateManager.endConversation();
        stateManager.endSession();
    }
}
