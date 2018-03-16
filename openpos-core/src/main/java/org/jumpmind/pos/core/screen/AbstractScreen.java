package org.jumpmind.pos.core.screen;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public abstract class AbstractScreen implements Serializable {

    private static final long serialVersionUID = 1L;

    public static String TEMPLATE_SELL = "Sell";
    public static String TEMPLATE_BLANK = "Blank";

    /**
     * Put properties in this map if they are optional. When not set, they don't
     * show up in the json which means less overhead.
     */
    private Map<String, Object> optionalProperties = new HashMap<String, Object>();

    private String name;
    private String type;
    private MenuItem backButton;
    private MenuItem logoutButton;
    private String template = "Blank";
    private int sequenceNumber;
    private String locale;
    private String subType;

    public AbstractScreen() {
    }

    public AbstractScreen(String name, String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Allows this screen content to be displayed in a Dialog on the client side.
     */
    public void asDialog() {
        this.setType(ScreenType.Dialog);
        this.setSubType(this.getType());
    }
    
    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }
    
    @JsonAnyGetter
    public Map<String, Object> any() {
        return this.optionalProperties;
    }

    @JsonAnySetter
    public void put(String name, Object value) {
        this.optionalProperties.put(name, value);
    }

    public boolean contains(String name) {
        return this.optionalProperties.containsKey(name);
    }

    public Object get(String name) {
        return optionalProperties.get(name);
    }

    public void clearAdditionalProperties() {
        this.optionalProperties.clear();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public boolean isScreenOfType(String type) {
        return this.type == type;
    }

    public void setBackButton(MenuItem backButton) {
        this.backButton = backButton;
    }

    public MenuItem getBackButton() {
        return backButton;
    }

    public void setLogoutButton(MenuItem logoutButton) {
        this.logoutButton = logoutButton;
    }

    public MenuItem getLogoutButton() {
        return logoutButton;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setTheme(String theme) {
        this.optionalProperties.put("theme", theme);
    }

    public void setReadOnly(boolean isReadOnly) {
        this.optionalProperties.put("readOnly", isReadOnly);
    }

    public void setUseOnScreenKeyboard(boolean useOnScreenKeyboard) {
        this.optionalProperties.put("useOnScreenKeyboard", useOnScreenKeyboard);
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
    
    public void setRefreshAlways(boolean refreshAlways) {
        this.optionalProperties.put("refreshAlways", refreshAlways);
    }
    

}
