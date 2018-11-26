package org.jumpmind.pos.translate;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Optional;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.jumpmind.pos.core.model.FieldInputType;
import org.jumpmind.pos.core.model.Form;
import org.jumpmind.pos.core.screen.Screen;
import org.jumpmind.pos.server.model.Action;
import org.jumpmind.pos.core.screen.IPromptScreen;
import org.jumpmind.pos.core.screen.IconType;

public abstract class AbstractPromptScreenTranslator<T extends Screen> extends AbstractLegacyScreenTranslator<T> {

    private boolean minMaxLengthRestrictionEnabled = true;
    
	public AbstractPromptScreenTranslator(ILegacyScreen legacyScreen, Class<T> screenClass) {
	    this(legacyScreen, screenClass, null, null);
	}
	
    public AbstractPromptScreenTranslator(ILegacyScreen legacyScreen, Class<T> screenClass, String appId, Properties properties) {
        super(legacyScreen, screenClass, appId, properties);
        if (!IPromptScreen.class.isAssignableFrom(screenClass)) {
            throw new IllegalArgumentException(String.format("screenClass %s must be assignable from %s", screenClass.getSimpleName(),
                    IPromptScreen.class.getSimpleName()));
        }
    }

    protected void configureScreenResponseField() {
        ILegacyUIModel baseModel = legacyPOSBeanService.getLegacyUIModel(legacyScreen);
        if (baseModel != null) {
            ILegacyAssignmentSpec promptResponseSpec = legacyPOSBeanService.getLegacyAssignmentSpec(legacyScreen, PROMPT_RESPONSE_PANEL_KEY);
            ILegacyPromptAndResponseModel promptAndResponseBeanModel = legacyPOSBeanService.getLegacyPromptAndResponseModel(legacyScreen);
            IPromptScreen promptScreen = getPromptScreen();

            String formattedPromptText = getPromptText(baseModel, promptResponseSpec, legacyScreen.getResourceBundleFilename()).orElse(null);

            String responseFieldType = getResponseFieldType(promptResponseSpec);

            String enterDataValue = promptResponseSpec.getPropertyValue("enterData");
            boolean enterData = "true".equals(enterDataValue != null ? enterDataValue.toLowerCase() : "false");

            // The promptAndResponseBeanModel will be returned by getSpecPropertyValue if it is set, otherwise it uses the spec value
            String minLength = getSpecPropertyValue(promptResponseSpec, "minLength", promptAndResponseBeanModel.getMinLength());
            String maxLength = getSpecPropertyValue(promptResponseSpec, "maxLength", promptAndResponseBeanModel.getMaxLength());

            if (isNotBlank(formattedPromptText)) {
                String text = formattedPromptText
                        .replace("Enter number and press Next", "")
                        .replace(" and press Next", "")
                        .replace(" and press next", "")
                        .replace(", then press Next", "");
                if (text.endsWith(".")) {
                    text = text.substring(0, text.length() - 1);
                }
                promptScreen.setText(text);
            }

            if (enterData) {
                promptScreen.setResponseText(promptAndResponseBeanModel.getResponseText());
                promptScreen.setEditable(true);
                // If the reponseType has already been set before getting here, don't override it.
                if(StringUtils.isEmpty(promptScreen.getResponseType()) ) {
                    promptScreen.setResponseType(responseFieldType);
                    if( responseFieldType.equals(IPromptScreen.TYPE_CURRENCYTEXT)) {
                        promptScreen.setResponseType(FieldInputType.Money.name());
                    }
                }
                
                // Only set the response min/max length if it hasn't already been set at this point.  Callers may have
                // already set values on the screen.
                if (this.isMinMaxLengthRestrictionEnabled()) {
                    if (minLength != null && promptScreen.getMinLength() == null) {
                        promptScreen.setMinLength(Integer.parseInt(minLength));
                    }
                    if (maxLength != null && promptScreen.getMaxLength() == null) {
                        promptScreen.setMaxLength(Integer.parseInt(maxLength));
                    }
                }
            } else {
                promptScreen.setEditable(false);
            }

            if (IPromptScreen.TYPE_ALPHANUMERICPASSWORD.equals(promptScreen.getResponseType())) {
                promptScreen.setPromptIcon(IconType.Login);
            } else if( IPromptScreen.TYPE_PHONE.equals(promptScreen.getResponseType())) {
            		promptScreen.setPromptIcon(IconType.Phone);
            } else if(FieldInputType.Money.name().equalsIgnoreCase(promptScreen.getResponseType())) {
                // TODO: I'd like to use cash icon here, but needs to have adjustment to styling for case when using
                // a local icon.  just use default icon for now
                // promptScreen.setPromptIcon(new LocalIcon("cash-multiple").getName());
                promptScreen.setPromptIcon(IconType.DefaultPrompt);
            }
            else {
                promptScreen.setPromptIcon(IconType.DefaultPrompt);
            }
        }

    }

    protected Optional<String> getPromptText(ILegacyUIModel uiModel, ILegacyAssignmentSpec promptResponsePanel,
            String resourceBundleFilename) {
        Optional<String> optPromptText = Optional.empty();
        try {
          
            ILegacyPromptAndResponseModel promptAndResponseModel = this.legacyPOSBeanService.getLegacyPromptAndResponseModel(legacyScreen);
            
            String formattedPromptText = promptAndResponseModel.getPromptText();
            
            if (isBlank(formattedPromptText)) {
                String promptTextTag = promptResponsePanel.getPropertyValue("promptTextTag");            
                String resourceText = this.legacyPOSBeanService.getLegacyUIUtilities().retrieveText(promptResponsePanel.getBeanSpecName(), resourceBundleFilename, promptTextTag);
                formattedPromptText = toFormattedString(resourceText, promptAndResponseModel != null ? promptAndResponseModel.getArguments() : null);
            }

            optPromptText = Optional.ofNullable(formattedPromptText);
        } catch (Exception ex) {
            logger.error("Failed to get promptText for {}", uiModel.getModel().getClass());
        }
        return optPromptText;
    }

    protected String getResponseFieldType(ILegacyAssignmentSpec promptResponseSpec) {
        String responseFieldType = getSpecPropertyValue(promptResponseSpec, "responseField", null);
        if (responseFieldType != null && responseFieldType.contains("Field")) {
            responseFieldType = responseFieldType.substring(responseFieldType.lastIndexOf(".") + 1, responseFieldType.indexOf("Field"))
                    .toUpperCase();
        } else {
            responseFieldType = IPromptScreen.TYPE_ALPHANUMERICTEXT;
        }

        return responseFieldType;
    }

    protected void handleNextAction(ITranslationManagerSubscriber subscriber, TranslationManagerServer tmServer, Action action, Form formResults) {
        String prompt = (action != null && action.getData() != null) ? action.getData().toString() : null;
        if (isNotBlank(prompt)) {
            setScreenResponseText(action.toDataString());
            tmServer.sendAction(action.getName());
        } else {
            super.handleAction(subscriber, tmServer, action, formResults);
        }
    }
    
    @Override
    public void handleAction(ITranslationManagerSubscriber subscriber, TranslationManagerServer tmServer, Action action, Form formResults) {
        if ("Next".equalsIgnoreCase(action.getName())) {
            handleNextAction(subscriber, tmServer, action, formResults);
        } else {
            super.handleAction(subscriber, tmServer, action, formResults);
        }
    }

    protected IPromptScreen getPromptScreen() {
        return (IPromptScreen) getScreen();
    }

    protected void setScreenResponseText(String responseText) {
        legacyPOSBeanService.getLegacyPromptAndResponseModel(legacyScreen).setResponseText(responseText);
    }

    public boolean isMinMaxLengthRestrictionEnabled() {
        return minMaxLengthRestrictionEnabled;
    }

    public void setMinMaxLengthRestrictionEnabled(boolean minMaxLengthRestrictionEnabled) {
        this.minMaxLengthRestrictionEnabled = minMaxLengthRestrictionEnabled;
    }

}
