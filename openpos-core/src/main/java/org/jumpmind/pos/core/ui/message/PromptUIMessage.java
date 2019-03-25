package org.jumpmind.pos.core.ui.message;

import org.jumpmind.pos.core.model.FieldInputType;
import org.jumpmind.pos.core.model.KeyboardType;
import org.jumpmind.pos.core.model.Validator;
import org.jumpmind.pos.core.screen.ActionItem;
import org.jumpmind.pos.core.ui.UIMessage;

import java.util.*;

public class PromptUIMessage extends UIMessage {
    private String promptIcon;
    private String placeholderText;
    private String hintText;
    private String instructions;
    private String responseText;
    private boolean editable = true;
    private FieldInputType responseType;
    private ActionItem actionButton = null;
    private List<ActionItem> otherActions;
    private Integer minLength;
    private Integer maxLength;
    private String comments = "";
    private boolean showComments = false;
    private String validationPattern;
    private boolean scanEnabled;

    public PromptUIMessage() {
        this.setScreenType(UIMessageType.PROMPT);
    }

    public String getPromptIcon() {
        return promptIcon;
    }

    public void setPromptIcon(String promptIcon) {
        this.promptIcon = promptIcon;
    }

    public String getPlaceholderText() {
        return placeholderText;
    }

    public void setPlaceholderText(String placeholderText) {
        this.placeholderText = placeholderText;
    }

    public String getHintText() {
        return hintText;
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getResponseText() {
        return  responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public FieldInputType getResponseType() {
        return responseType;
    }

    public void setResponseType(FieldInputType responseType) {
        this.responseType = responseType;
    }

    public ActionItem getActionButton() {
        return actionButton;
    }

    public void setActionButton(ActionItem actionButton) {
        this.actionButton = actionButton;
    }

    public List<ActionItem> getOtherActions() {
        return otherActions;
    }

    public void setOtherActions(List<ActionItem> otherActions) {
        this.otherActions = otherActions;
    }

    public void addOtherAction(ActionItem action) {
        if (this.otherActions == null) {
            this.otherActions = new ArrayList<>();
        }
        this.otherActions.add(action);
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isShowComments() {
        return showComments;
    }

    public void setShowComments(boolean showComments) {
        this.showComments = showComments;
    }

    public String getValidationPattern() {
        return validationPattern;
    }

    public void setValidationPattern(String validationPattern) {
        this.validationPattern = validationPattern;
    }

    public boolean isScanEnabled() {
        return scanEnabled;
    }

    public void setScanEnabled(boolean scanEnabled) {
        this.scanEnabled = scanEnabled;
    }

    public void setKeyboardPreference(KeyboardType keyboardPreference) {
        this.put("keyboardPreference", keyboardPreference);
    }

    public void setValidators(Set<Validator> validators) {
        this.put("validators", validators);
    }

    public void addValidators(Validator ...validators) {
        if (validators != null && validators.length > 0) {
            if (! this.contains("validators")) {
                this.put("validators", new HashSet<Validator>());
            }
            @SuppressWarnings("unchecked")
            Set<Validator> theValidators = (Set<Validator>) this.get("validators");
            theValidators.addAll(Arrays.asList(validators));
        }
    }
}
