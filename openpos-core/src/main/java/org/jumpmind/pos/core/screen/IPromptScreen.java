package org.jumpmind.pos.core.screen;

public interface IPromptScreen {
    public static final String TYPE_ALPHANUMERICTEXT = "ALPHANUMERICTEXT";
    public static final String TYPE_ALPHANUMERICPASSWORD = "ALPHANUMERICPASSWORD";
    public static final String TYPE_CURRENCYTEXT = "CURRENCYTEXT";
    public static final String TYPE_NUMERICTEXT = "NUMERICTEXT";
    public static final String TYPE_DATE = "DATE";

    public int getMinLength();
    public void setMinLength(int minLength);

    public int getMaxLength();
    public void setMaxLength(int maxLength);

    public String getPromptIcon();
    public void setPromptIcon(String promptIcon);

    public String getPlaceholderText();
    public void setPlaceholderText(String placeholderText);

    public String getResponseText();
    public void setResponseText(String responseText);

    public boolean isEditable();
    public void setEditable(boolean editable);
    
    public String getResponseType();
    public void setResponseType(String responseType);
    
    public String getText();
    public void setText(String promptText);
    
}
