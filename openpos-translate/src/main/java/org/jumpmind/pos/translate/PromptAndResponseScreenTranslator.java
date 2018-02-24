package org.jumpmind.pos.translate;

import java.util.List;
import java.util.Properties;

import org.jumpmind.pos.core.model.FieldInputType;
import org.jumpmind.pos.core.model.IMaskSpec;
import org.jumpmind.pos.core.screen.DefaultScreen;
import org.jumpmind.pos.core.screen.MenuItem;
import org.jumpmind.pos.core.screen.PromptScreen;

public class PromptAndResponseScreenTranslator<T extends PromptScreen> extends AbstractPromptScreenTranslator<T> {

    private boolean addLocalMenuItems = false;

    public PromptAndResponseScreenTranslator(ILegacyScreen legacyScreen, Class<T> screenClass, boolean addLocalMenuItems) {
        this(legacyScreen, screenClass, addLocalMenuItems, (FieldInputType) null);
    }

    public PromptAndResponseScreenTranslator(ILegacyScreen legacyScreen, Class<T> screenClass, boolean addLocalMenuItems, String appId, Properties properties) {
        this(legacyScreen, screenClass, addLocalMenuItems, (FieldInputType) null, null, null, appId, properties);
    }
    
    /**
     * 
     * @param legacyScreen
     * @param screenClass
     * @param addLocalMenuItems
     * @param promptMask
     * @deprecated IMaskSpec is no longer used for formatting values in the prompt and response field.  Use
     * the constructor that accepts a FieldInputType instead.  Support FieldInputTypes at the time of this writing are:
     * Money, Phone, Percent, NumericText, AlphaNumericText 
     */
    @Deprecated
    public PromptAndResponseScreenTranslator(ILegacyScreen legacyScreen, Class<T> screenClass, boolean addLocalMenuItems,
            IMaskSpec promptMask) {
        this(legacyScreen, screenClass, addLocalMenuItems, promptMask, null, null);
    }
    
    public PromptAndResponseScreenTranslator(ILegacyScreen legacyScreen, Class<T> screenClass, boolean addLocalMenuItems,
            FieldInputType responseType) {
        this(legacyScreen, screenClass, addLocalMenuItems, responseType, null, null);
    }

    public PromptAndResponseScreenTranslator(ILegacyScreen legacyScreen, Class<T> screenClass, boolean addLocalMenuItems,
            FieldInputType responseType, Integer minLength, Integer maxLength) {
        this(legacyScreen, screenClass, addLocalMenuItems, responseType, minLength, maxLength, null, null);
    }
    

    public PromptAndResponseScreenTranslator(ILegacyScreen legacyScreen, Class<T> screenClass, boolean addLocalMenuItems,
            FieldInputType responseType, Integer minLength, Integer maxLength, String appId, Properties properties) {
        super(legacyScreen, screenClass, appId, properties);
        getScreen().setTemplate(DefaultScreen.TEMPLATE_SELL);
        this.addLocalMenuItems = addLocalMenuItems;
        getScreen().setResponseType(responseType != null ? responseType.name() : null);
        getScreen().setMinLength(minLength);
        getScreen().setMaxLength(maxLength);
    }
    
   /**
    * @deprecated IMaskSpec is no longer required for formatting values in the prompt and response field.  Use
    * the constructor that accepts a FieldInputType instead.  Support FieldInputTypes at the time of this writing are:
    * Money, Phone, Percent, NumericText, AlphaNumericText 
    */
   @Deprecated
    public PromptAndResponseScreenTranslator(ILegacyScreen legacyScreen, Class<T> screenClass, boolean addLocalMenuItems,
            IMaskSpec promptMask, Integer minLength, Integer maxLength) {
        this(legacyScreen, screenClass, addLocalMenuItems, promptMask, minLength, maxLength, null, null);
    }

   /**
    * @deprecated IMaskSpec is no longer required for formatting values in the prompt and response field.  Use
    * the constructor that accepts a FieldInputType instead.  Support FieldInputTypes at the time of this writing are:
    * Money, Phone, Percent, NumericText, AlphaNumericText 
    */
    public PromptAndResponseScreenTranslator(ILegacyScreen legacyScreen, Class<T> screenClass, boolean addLocalMenuItems,
            IMaskSpec promptMask, Integer minLength, Integer maxLength, String appId, Properties properties) {
        super(legacyScreen, screenClass, appId, properties);
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
        getScreen().setActionButton(new MenuItem("Next", "Next", "keyboard_arrow_right"));

    }

}
