package org.jumpmind.pos.translate;

import org.jumpmind.pos.core.screen.DefaultScreen;
import org.jumpmind.pos.core.screen.MenuItem;
import org.jumpmind.pos.core.screen.PromptScreen;

public class PromptAndResponseScreenTranslator<T extends PromptScreen> extends AbstractPromptScreenTranslator<T> {

    private boolean addLocalMenuItems = false;
    
    public PromptAndResponseScreenTranslator(ILegacyScreen headlessScreen, Class<T> screenClass, boolean addLocalMenuItems) {
        super(headlessScreen, screenClass);
        screen.setTemplate(DefaultScreen.TEMPLATE_SELL);
        this.addLocalMenuItems = addLocalMenuItems;
    }

    @Override
    protected void buildMainContent() {
        super.buildMainContent();
        screen.setRefreshAlways(true);
        this.configureScreenResponseField();
        if (addLocalMenuItems) {
            screen.setLocalMenuItems(generateUIActionsForLocalNavButtons(MenuItem.class, true));    
        }
        
    }

}
