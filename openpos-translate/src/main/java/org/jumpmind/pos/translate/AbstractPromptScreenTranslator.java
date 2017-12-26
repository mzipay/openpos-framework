package org.jumpmind.pos.translate;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.Locale;
import java.util.Optional;
import java.util.Properties;

import org.jumpmind.pos.core.flow.Action;
import org.jumpmind.pos.core.screen.DefaultScreen;
import org.jumpmind.pos.core.screen.IPromptScreen;

public abstract class AbstractPromptScreenTranslator<T extends DefaultScreen> extends AbstractLegacyScreenTranslator<T> {

    public AbstractPromptScreenTranslator(ILegacyScreen headlessScreen, Class<T> screenClass) {
        super(headlessScreen, screenClass);
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

            boolean enterData = "true".equals(promptResponseSpec.getPropertyValue("enterData").toLowerCase());

            String minLength = getSpecPropertyValue(promptResponseSpec, "minLength", promptAndResponseBeanModel.getMinLength());
            String maxLength = getSpecPropertyValue(promptResponseSpec, "maxLength", promptAndResponseBeanModel.getMaxLength());

            if (isNotBlank(formattedPromptText)) {
                String text = formattedPromptText.replace(" and press Next", "").replace(" and press next", "");
                if (text.endsWith(".")) {
                    text = text.substring(0, text.length() - 1);
                }
                promptScreen.setText(text);
            }

            if (enterData) {
                promptScreen.setResponseText(promptAndResponseBeanModel.getResponseText());
                promptScreen.setEditable(true);
                promptScreen.setResponseType(responseFieldType);
                if (promptAndResponseBeanModel.getMinLength() != null) {
                    promptScreen.setMinLength(Integer.parseInt(minLength));
                }
                if (promptAndResponseBeanModel.getMaxLength() != null) {
                    promptScreen.setMaxLength(Integer.parseInt(maxLength));
                }
            } else {
                promptScreen.setEditable(false);
            }

            if (IPromptScreen.TYPE_ALPHANUMERICPASSWORD.equals(promptScreen.getResponseType())) {
                promptScreen.setPromptIcon("lock");
            } else {
                promptScreen.setPromptIcon("question_answer");
            }
        }

    }

    protected Optional<String> getPromptText(ILegacyUIModel uiModel, ILegacyAssignmentSpec promptResponsePanel,
            String resourceBundleFilename) {
        Optional<String> optPromptText = Optional.empty();
        try {
            ILegacyPromptAndResponseModel promptAndResponseModel = this.legacyPOSBeanService.getLegacyPromptAndResponseModel(legacyScreen);

            String promptTextTag = promptResponsePanel.getPropertyValue("promptTextTag");
            Properties resourceBundle = this.legacyPOSBeanService.getLegacyResourceBundleUtil().getText(resourceBundleFilename,
                    Locale.getDefault());
            String promptTextKey = String.format("%s.%s", "PromptAndResponsePanelSpec", promptTextTag);
            String formattedPromptText = null;
            formattedPromptText = toFormattedString(resourceBundle, promptTextKey,
                    promptAndResponseModel != null ? Optional.ofNullable(promptAndResponseModel.getArguments()) : Optional.empty());
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

    @Override
    public void handleAction(ITranslationManagerSubscriber subscriber, TranslationManagerServer tmServer, Action action, DefaultScreen screen) {
        if ("Next".equalsIgnoreCase(action.getName())) {
            String prompt = (action != null && action.getData() != null) ? action.getData().toString() : null;
            if (isNotBlank(prompt)) {
                setScreenResponseText(action.toDataString());
                tmServer.sendAction(action.getName());
            } else {
                super.handleAction(subscriber, tmServer, action, screen);
            }
        } else {
            super.handleAction(subscriber, tmServer, action, screen);
        }
    }

    protected IPromptScreen getPromptScreen() {
        return (IPromptScreen) getScreen();
    }

    protected void setScreenResponseText(String responseText) {
        legacyPOSBeanService.getLegacyPromptAndResponseModel(legacyScreen).setResponseText(responseText);
    }

}
