package org.jumpmind.jumppos.core.screen;

public class PromptScreen extends DefaultScreen {

    private static final long serialVersionUID = 1L;
    
    public static final String TYPE_ALPHANUMERICTEXT = "ALPHANUMERICTEXT";
    public static final String TYPE_ALPHANUMERICPASSWORD = "ALPHANUMERICPASSWORD";
    public static final String TYPE_CURRENCYTEXT = "CURRENCYTEXT";
    public static final String TYPE_NUMERICTEXT = "NUMERICTEXT";
    
    private String promptIcon;
    private String placeholderText;
    private String text;
    private String responseText;
    private boolean editable = true;
    private String responseType;
    private int minLength;
    private int maxLength;
    private String action = "Next";

    public PromptScreen() {
        setType(PROMPT_SCREEN_TYPE);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getResponseText() {
        return responseText;
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

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
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
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }

}
