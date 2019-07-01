package org.jumpmind.pos.core.service;

import java.util.Locale;

import org.jumpmind.pos.core.clientconfiguration.LocaleChangedMessage;
import org.jumpmind.pos.core.clientconfiguration.LocaleMessageFactory;
import org.jumpmind.pos.core.flow.IStateManager;
import org.jumpmind.pos.core.flow.In;
import org.jumpmind.pos.core.flow.ScopeType;
import org.jumpmind.pos.server.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("device")
public class ClientLocaleService {

    @In(scope = ScopeType.Device)
    IStateManager stateManager;

    @Autowired
    MessageService messageService;

    @Autowired
    LocaleMessageFactory localeMessageFactory;

    private Locale locale;

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        LocaleChangedMessage message = localeMessageFactory.getMessage(locale);
        messageService.sendMessage(stateManager.getAppId(), stateManager.getDeviceId(), message);
    }

}
