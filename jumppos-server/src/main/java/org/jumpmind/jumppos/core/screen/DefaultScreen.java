package org.jumpmind.jumppos.core.screen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public class DefaultScreen implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public static final String CHOOSE_OPTIONS_SCREEN_TYPE = "ChooseOptions";
    public static final String DIALOG_SCREEN_TYPE = "Dialog";
    public static final String FORM_SCREEN_TYPE = "Form";
    public static final String PAYMENT_STATUS = "PaymentStatus";
    public static final String PROMPT_SCREEN_TYPE = "Prompt";
    public static final String SELL_ITEM_DETAIL_SCREEN_TYPE = "SellItemDetail";
    public static final String SELL_SCREEN_TYPE = "Sell";

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();    
    private String name;    
    private String type;    
    private MenuItem backButton;
    
    private List<MenuItem> menuActions = new ArrayList<>();
    
    public DefaultScreen() {
    }

    public DefaultScreen(String name) {
        put("name", name);
    }
    
    @JsonAnyGetter
    public Map<String, Object> any() {
       return this.additionalProperties;
    }

    @JsonAnySetter
    public void put(String name, Object value) {
       this.additionalProperties.put(name, value);
    }
    
    public boolean contains(String name) {
        return this.additionalProperties.containsKey(name);
    }
    
    public Object get(String name) {
        return additionalProperties.get(name);
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
        return type;
    }

    @SuppressWarnings("unchecked")
    public void addToGroup(String groupName, String dataName, Object value) {
        Object group = get(groupName);
        Map<String, Object> map = null;
        if (group == null || !(group instanceof Map)) {
            map = new HashMap<>();
            put(groupName, map);
        } else {
            map = (Map<String, Object>) group;
        }
        map.put(dataName, value);
    }

    @SuppressWarnings("unchecked")
    public void addToList(String dataName, Object value) {
        Object obj = get(dataName);
        List<Object> list = null;
        if (obj == null || !(obj instanceof List)) {
            list = new ArrayList<>();
            put(dataName, list);
        } else {
            list = (List<Object>) obj;
        }
        if (!list.contains(value)) {
            list.add(value);
        }
    }
    
    public void setBackButton(MenuItem backButton) {
        this.backButton = backButton;
    }
    
    public MenuItem getBackButton() {
        return backButton;
    }
    
    public List<MenuItem> getMenuActions() {
        return menuActions;
    }
    
    public void setMenuActions(List<MenuItem> menuActions) {
        this.menuActions = menuActions;
    }

}
