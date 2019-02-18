package org.jumpmind.pos.translate.state;

import org.jumpmind.pos.core.flow.ActionHandler;
import org.jumpmind.pos.core.model.Form;
import org.jumpmind.pos.server.model.Action;

public class OrPosState extends TranslatorState {

    @ActionHandler
    public void onUndo(Action action, Form form) {
        super.onAnyAction(action, form);
    }

}
