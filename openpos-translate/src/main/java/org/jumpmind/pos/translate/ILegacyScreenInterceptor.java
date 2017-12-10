package org.jumpmind.pos.translate;

import org.jumpmind.pos.core.model.POSSessionInfo;

public interface ILegacyScreenInterceptor {

    public boolean intercept(ILegacyScreen screen, ILegacyScreen previousScreen, ITranslationManagerSubscriber subscriber,
            TranslationManagerServer tmServer, POSSessionInfo sessionInfo);
}
