package org.jumpmind.pos.translate;

import java.util.List;
import java.util.Optional;

import org.jumpmind.pos.core.screen.OptionItem;
import org.jumpmind.pos.core.screen.PromptWithOptionsScreen;

/**
 * General purpose translator that results in rendering a screen with the OrPOS
 * prompt text, an input text field, and a list of mutually exclusive options
 * for the cashier to select.
 */
public class PromptWithOptionsScreenTranslator<T extends PromptWithOptionsScreen> extends AbstractPromptScreenTranslator<T> {

    public PromptWithOptionsScreenTranslator(ILegacyScreen headlessScreen, Class<T> screenClass) {
        super(headlessScreen, screenClass);
    }

    @Override
    protected void buildMainContent() {
        super.buildMainContent();
        this.buildOptions();
        this.configureScreenResponseField();
        getScreen().setPrompt(getScreen().getText());
    }

    protected void buildOptions() {
        List<ILegacyButtonSpec> allOptions = this.getPanelButtons(LOCAL_NAV_PANEL_KEY, Optional.of(true));
        ILegacyAssignmentSpec localNavPanel = legacyPOSBeanService.getLegacyAssignmentSpec(legacyScreen, LOCAL_NAV_PANEL_KEY);

        allOptions.stream().forEach(t -> {
            String buttonText = legacyPOSBeanService.getLegacyUtilityManager(legacyScreen).retrieveText(localNavPanel.getBeanSpecName(),
                    getResourceBundleFilename(), t.getLabelTag(), t.getLabelTag());
            OptionItem i = new OptionItem(t.getActionName(), buttonText, t.getEnabled());
            screen.addOption(i);
        });

    }
}
