package org.jumpmind.pos.core.screen;

import java.util.ArrayList;
import java.util.List;


public class PromptWithOptionsScreen extends ChooseOptionsScreen implements IPromptScreen {
    private static final long serialVersionUID = 1L;

    private String promptIcon;
    private String placeholderText;
    private boolean editable = true;
    private String responseType;
    private String responseText;
    private int minLength;
    private int maxLength;
    private String action = "Next";

    public PromptWithOptionsScreen() {
        this(new ArrayList<>());
    }

    public PromptWithOptionsScreen(List<OptionItem> options) {
        this(options, SelectionMode.Single);
    }

    public PromptWithOptionsScreen(List<OptionItem> options, SelectionMode selectionMode) {
        super(options, selectionMode);
        setType(ScreenType.PromptWithOptions);
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

    @Override
    public String getResponseText() {
        return responseText;
    }

    @Override
    public void setResponseText(String responseText) {
        this.responseText = responseText;
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
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @Override
    public String getText() {
        return this.getPromptText();
    }

    @Override
    public void setText(String promptText) {
        this.setPromptText(promptText);
    }
}
