package org.jumpmind.pos.translate;

import java.util.HashSet;
import java.util.Set;

import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.screen.DefaultScreen;
import org.jumpmind.pos.core.screen.MenuItem;
import org.jumpmind.pos.core.screen.ScreenType;

public class SellOptionsTranslator extends AbstractLegacyScreenTranslator<DefaultScreen> {

    Set<String> excludeLabelTags = new HashSet<>();
    
    InteractionMacro undoMacro;

    public SellOptionsTranslator(ILegacyScreen headlessScreen, String icon, String... excludeActions) {
        super(headlessScreen, DefaultScreen.class);
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
    
    public void setUndoMacro(InteractionMacro undoMacro) {
        this.undoMacro = undoMacro;
    }

    @Override
    protected void buildMainContent() {
        super.buildMainContent();
        screen.setTemplate(DefaultScreen.TEMPLATE_SELL);
        screen.setLocalMenuItems(generateUIActionsForLocalNavButtons(MenuItem.class, true));
    }
    
    @Override
    public void handleAction(ITranslationManagerSubscriber subscriber, TranslationManagerServer tmServer, Action action,
            DefaultScreen screen) {
        if ("Undo".equals(action.getName()) && undoMacro != null) {
            tmServer.executeMacro(undoMacro);
        } else {
            super.handleAction(subscriber, tmServer, action, screen);
        }
    }

}
