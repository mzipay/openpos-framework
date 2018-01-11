package org.jumpmind.pos.core.screen;


import org.jumpmind.pos.core.model.IMaskSpec;

public class PromptScreen extends DefaultScreen implements IPromptScreen {

    private static final long serialVersionUID = 1L;
    
    private String promptIcon;
    private String placeholderText;
    private String text;
    private String responseText;
    private boolean editable = true;
    private String responseType;
    private int minLength;
    private int maxLength;
    private String action = "Next";
    private MenuItem actionButton = null;
    private IMaskSpec promptMask;

    public PromptScreen() {
        setType(ScreenType.Prompt);
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getResponseText() {
        return responseText;
    }

    @Override
    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @Override
    public String getResponseType() {
        return responseType;
    }

    @Override
    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    @Override
    public int getMinLength() {
        return minLength;
    }

    @Override
    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    @Override
    public int getMaxLength() {
        return maxLength;
    }

    @Override
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public String getPromptIcon() {
        return promptIcon;
    }

    @Override
    public void setPromptIcon(String promptIcon) {
        this.promptIcon = promptIcon;
    }

    @Override
    public String getPlaceholderText() {
        return placeholderText;
    }

    @Override
    public void setPlaceholderText(String placeholderText) {
        this.placeholderText = placeholderText;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }

    public MenuItem getActionButton() {
        return actionButton;
    }

    public void setActionButton(MenuItem actionButton) {
        this.actionButton = actionButton;
    }
    
    public IMaskSpec getPromptMask() {
        return this.promptMask;
    }
    
    public void setPromptMask(IMaskSpec mask) {
        this.promptMask = mask;
    }
    
}
