package org.jumpmind.pos.core.screen;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractScreen implements Serializable {

    private static final long serialVersionUID = 1L;

    public static String TEMPLATE_SELL = "Sell";
    public static String TEMPLATE_BLANK = "Blank";
    public static String TEMPLATE_BLANK_WITH_BAR = "BlankWithBar";
    public static String TEMPLATE_SELF_CHECKOUT = "SelfCheckout";

    /**
     * Put properties in this map if they are optional. When not set, they don't
     * show up in the json which means less overhead.
     */
    private Map<String, Object> optionalProperties = new HashMap<String, Object>();

    private String name;
    private String type;
    private String template = TEMPLATE_BLANK_WITH_BAR;
    private String locale;
    private String subType;
    
    public AbstractScreen() {
    }

    public AbstractScreen(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public static <T> T convertActionData(Object actionData, Class<T> convertToInstanceOf) {
        return new ObjectMapper().convertValue(actionData, convertToInstanceOf);
    }
    
    /**
     * Allows this screen content to be displayed in a Dialog on the client side.
     */
    public AbstractScreen asDialog() {
        return this.asDialog(null);
    }

    /**
     * Allows this screen content to be displayed in a Dialog on the client side.
     * @param dialogProperties Additional properties that can control dialog behavior and rendering on the server side.
     */
    public AbstractScreen asDialog(DialogProperties dialogProperties) {
        this.setSubType(this.getType());
        this.setType(ScreenType.Dialog);
        if (dialogProperties != null) {
            this.put("dialogProperties", dialogProperties);
        }
        return this;
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
        put("backButton", backButton);
    }

    public void setLogoutButton(MenuItem logoutButton) {
        put("logoutButton", logoutButton);
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

    public void setSequenceNumber(int sequenceNumber) {
        put("sequenceNumber", sequenceNumber);
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
