package org.jumpmind.pos.translate;

import org.jumpmind.pos.core.screen.LoadingScreen;

public class StatusScreenTranslator extends AbstractLegacyScreenTranslator<LoadingScreen> {
    public enum Mode {
        /** Displays the given title and message or uses values from legacy resources if title and message not given. */
        Default,
        /** Moves the message text to the title area and eliminates the message text. */
        UseMessageForTitle 
    }
    
    private Mode mode = Mode.Default;
    
    public StatusScreenTranslator(ILegacyScreen legacyScreen) {
        this(legacyScreen, Mode.Default);
    }

    public StatusScreenTranslator(ILegacyScreen legacyScreen, Mode mode) {
        this(legacyScreen, (String) null);
        this.mode = mode;
    }
    
    public StatusScreenTranslator(ILegacyScreen legacyScreen, String title) {
        this(legacyScreen, title, (String)null);
    }

    public StatusScreenTranslator(ILegacyScreen legacyScreen, String title, String message) {
        super(legacyScreen, LoadingScreen.class);
        getScreen().setTitle(title);
        getScreen().setMessage(message);
    }

    @Override
    public void buildMainContent() {
       super.buildMainContent();
       
       String statusText = this.getPromptText(this.getLegacyUIModel(), this.getLegacyAssignmentSpec(PROMPT_RESPONSE_PANEL_KEY), 
               this.getResourceBundleFilename()).orElse(null);
       
       if (getMode() == Mode.UseMessageForTitle) {
           getScreen().setTitle(this.hasMessage() ? getScreen().getMessage() : statusText);
           getScreen().setMessage((String) null);
       } else { // Default
           
           if (getScreen().getTitle() == null) {
               getScreen().setTitle(this.getScreenName());
           }
           
           // If a message hasn't already been set, just use what might be available as the Prompt text
           if (! this.hasMessage()) {
               getScreen().setMessage(statusText);
           }
       }        
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }
    
    protected boolean hasTitle() {
        return getScreen().getTitle() != null;
    }
    
    protected boolean hasMessage() {
        return getScreen().getMessage() != null;
    }
}
