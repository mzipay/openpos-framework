package org.jumpmind.pos.translate;

import org.jumpmind.pos.core.model.Form;
import org.jumpmind.pos.core.model.POSSessionInfo;
import org.jumpmind.pos.server.model.Action;

public interface ITranslator {

    public void handleAction(ITranslationManagerSubscriber subscriber, TranslationManagerServer tmServer, Action action, Form formResults);
    
    public void setPosSessionInfo(POSSessionInfo posSessionInfo);

}
