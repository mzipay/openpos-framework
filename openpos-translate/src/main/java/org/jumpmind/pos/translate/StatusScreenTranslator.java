package org.jumpmind.pos.translate;

import java.util.Arrays;
import java.util.List;

import org.jumpmind.pos.core.screen.LoadingScreen;

public class StatusScreenTranslator extends AbstractLegacyScreenTranslator<LoadingScreen> {

    public StatusScreenTranslator(ILegacyScreen legacyScreen) {
        this(legacyScreen, null);
    }

    public StatusScreenTranslator(ILegacyScreen legacyScreen, String title) {
        this(legacyScreen, title, (String)null);
    }

    public StatusScreenTranslator(ILegacyScreen legacyScreen, String title, String message) {
        this(legacyScreen, title, message != null ? Arrays.asList(message) : null);
    }
    
    public StatusScreenTranslator(ILegacyScreen legacyScreen, String title, String... messages) {
        this(legacyScreen, title, Arrays.asList(messages));
    }

    public StatusScreenTranslator(ILegacyScreen legacyScreen, String title, List<String> message) {
        super(legacyScreen, LoadingScreen.class);
        getScreen().setTitle(title);
        getScreen().setMessage(message);
    }

    
    @Override
    public void buildMainContent() {
       super.buildMainContent();
       String statusText = this.getPromptText(this.getLegacyUIModel(), this.getLegacyAssignmentSpec(PROMPT_RESPONSE_PANEL_KEY), 
                this.getResourceBundleFilename()).orElse(null);
       // If a message hasn't already been set, just use what might be available as the Prompt text
       if (getScreen().getMessage().size() == 0) {
           getScreen().setMessage(statusText);
       }
        
    }
}
