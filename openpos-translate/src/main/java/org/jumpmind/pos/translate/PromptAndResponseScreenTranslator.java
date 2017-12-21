package org.jumpmind.pos.translate;

import org.jumpmind.pos.core.screen.DefaultScreen;
import org.jumpmind.pos.core.screen.PromptScreen;

public class PromptAndResponseScreenTranslator<T extends PromptScreen> extends AbstractPromptScreenTranslator<T> {

    public PromptAndResponseScreenTranslator(ILegacyScreen headlessScreen, Class<T> screenClass) {
        super(headlessScreen, screenClass);
        screen.setTemplate(DefaultScreen.TEMPLATE_SELL);
    }

    @Override
    protected void buildMainContent() {
        super.buildMainContent();
        screen.setRefreshAlways(true);
        this.configureScreenResponseField();
    }

}
