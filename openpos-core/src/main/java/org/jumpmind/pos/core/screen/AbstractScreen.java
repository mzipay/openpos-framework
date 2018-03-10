package org.jumpmind.pos.core.screen;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public abstract class AbstractScreen  implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    
    private String name;    
    private String type;
    private MenuItem backButton;
    private MenuItem logoutButton;
    private String template = "Blank";
    private int sequenceNumber;
    
    public AbstractScreen() {
    }
       
    public AbstractScreen(String name, String type) {
        this.name = name;
        this.type = type;
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
    
    public void clearAdditionalProperties() {
        this.additionalProperties.clear();
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
    

}
