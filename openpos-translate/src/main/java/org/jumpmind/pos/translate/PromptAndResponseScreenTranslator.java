package org.jumpmind.pos.translate;

import java.util.List;

import org.jumpmind.pos.core.model.IMaskSpec;
import org.jumpmind.pos.core.screen.DefaultScreen;
import org.jumpmind.pos.core.screen.MenuItem;
import org.jumpmind.pos.core.screen.PromptScreen;

public class PromptAndResponseScreenTranslator<T extends PromptScreen> extends AbstractPromptScreenTranslator<T> {

    private boolean addLocalMenuItems = false;

    public PromptAndResponseScreenTranslator(ILegacyScreen legacyScreen, Class<T> screenClass, boolean addLocalMenuItems) {
        this(legacyScreen, screenClass, addLocalMenuItems, (IMaskSpec) null);
    }

    public PromptAndResponseScreenTranslator(ILegacyScreen legacyScreen, Class<T> screenClass, boolean addLocalMenuItems, String appId) {
        this(legacyScreen, screenClass, addLocalMenuItems, null, null, null, appId);
    }

    public PromptAndResponseScreenTranslator(ILegacyScreen legacyScreen, Class<T> screenClass, boolean addLocalMenuItems,
            IMaskSpec promptMask) {
        this(legacyScreen, screenClass, addLocalMenuItems, promptMask, null, null);
    }

    public PromptAndResponseScreenTranslator(ILegacyScreen legacyScreen, Class<T> screenClass, boolean addLocalMenuItems,
            IMaskSpec promptMask, Integer minLength, Integer maxLength) {
        this(legacyScreen, screenClass, addLocalMenuItems, promptMask, minLength, maxLength, null);
    }

    public PromptAndResponseScreenTranslator(ILegacyScreen legacyScreen, Class<T> screenClass, boolean addLocalMenuItems,
            IMaskSpec promptMask, Integer minLength, Integer maxLength, String appId) {
        super(legacyScreen, screenClass);
        getScreen().setTemplate(DefaultScreen.TEMPLATE_SELL);
        this.addLocalMenuItems = addLocalMenuItems;
        getScreen().setPromptMask(promptMask);
        getScreen().setMinLength(minLength);
        getScreen().setMaxLength(maxLength);
    }

    @Override
    protected void buildMainContent() {
        super.buildMainContent();
        screen.setRefreshAlways(true);
        this.configureScreenResponseField();
        if (addLocalMenuItems) {
            List<MenuItem> localNavButtons = generateUIActionsForLocalNavButtons(MenuItem.class, true);
            screen.setLocalMenuItems(localNavButtons);
        }
        getScreen().setActionButton(new MenuItem("Next", "Next", "arrow_forward"));

    }

}
