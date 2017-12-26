package org.jumpmind.pos.translate;

import java.util.HashSet;
import java.util.Set;

import org.jumpmind.pos.core.screen.DefaultScreen;
import org.jumpmind.pos.core.screen.MenuItem;
import org.jumpmind.pos.core.screen.ScreenType;

public class SellOptionsTranslator extends AbstractLegacyScreenTranslator<DefaultScreen> {

    Set<String> excludeLabelTags = new HashSet<>();

    public SellOptionsTranslator(ILegacyScreen headlessScreen, Class<DefaultScreen> screenClass, String icon, String... excludeActions) {
        super(headlessScreen, screenClass);
        if (excludeActions != null) {
            for (String string : excludeActions) {
                this.excludeLabelTags.add(string);
            }
        }
        screen.setType(ScreenType.Options);
        screen.setPrompt("Choose Option");
        screen.setIcon(icon);
        screen.setInstructions("Please select an option from the menu to the right");
    }

    @Override
    protected void buildMainContent() {
        super.buildMainContent();
        screen.setTemplate(DefaultScreen.TEMPLATE_SELL);
        screen.setLocalMenuItems(generateUIActionsForLocalNavButtons(MenuItem.class, true));
    }

}
