package org.jumpmind.pos.translate;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jumpmind.pos.core.screen.ChooseOptionsScreen;
import org.jumpmind.pos.core.screen.OptionItem;
import org.jumpmind.pos.core.screen.ScreenType;

public class ChooseOptionsScreenTranslator<T extends ChooseOptionsScreen> extends AbstractLegacyScreenTranslator<T> { 
    
    protected Function<OptionItem, Boolean> optionItemEvalFunc = null;
    
    public ChooseOptionsScreenTranslator(ILegacyScreen headlessScreen, Class<T> screenClass) {
        this(headlessScreen, screenClass, null);
    }

    /**
     * Constructor to be used for modifying OptionItems or excluding them.
     *  
     * @param headlessScreen The legacy screen,
     * @param screenClass The screenClass
     * @param optionFilter A function that can be used for modifying options or excluding them.  The given function will be invoked
     * for each OptionItem.  If the function returns false, the item will be excluded.
     */
    public ChooseOptionsScreenTranslator(ILegacyScreen headlessScreen, Class<T> screenClass, Function<OptionItem, Boolean> optionFilter) {
        super(headlessScreen, screenClass);
        this.optionItemEvalFunc = optionFilter;
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
        if (this.optionItemEvalFunc != null) {
            options = options.stream().filter(o -> { return this.optionItemEvalFunc.apply(o); }).collect(Collectors.toList());
        }
        getScreen().setOptions(options);
    }
}
