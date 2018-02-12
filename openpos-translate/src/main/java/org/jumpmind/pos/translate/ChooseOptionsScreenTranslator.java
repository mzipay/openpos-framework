package org.jumpmind.pos.translate;

import java.util.List;

import org.jumpmind.pos.core.screen.ChooseOptionsScreen;
import org.jumpmind.pos.core.screen.OptionItem;
import org.jumpmind.pos.core.screen.ScreenType;

public class ChooseOptionsScreenTranslator<T extends ChooseOptionsScreen> extends AbstractLegacyScreenTranslator<T> { 
    
    public ChooseOptionsScreenTranslator(ILegacyScreen headlessScreen, Class<T> screenClass) {
        super(headlessScreen, screenClass);
        getScreen().setType(ScreenType.ChooseOptions);
    }

    @Override
    protected void buildMainContent() {
        super.buildMainContent();
        this.buildOptions();
       
        String formattedPromptText = this.getPromptText(this.getLegacyUIModel(), this.getLegacyAssignmentSpec(PROMPT_RESPONSE_PANEL_KEY), 
                legacyScreen.getResourceBundleFilename()).orElse(null);
        getScreen().setPromptText(formattedPromptText);
    }
    
    protected void buildOptions() {
        List<OptionItem> options = this.generateUIActionsForLocalNavButtons(OptionItem.class, true, new String[]{});
        getScreen().setOptions(options);
    }
}
