package org.jumpmind.jumppos.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public abstract class Screen implements IScreen {

    protected Map<String, Object> additionalProperties = new HashMap<String, Object>();
    
    String name;
    
    String type;
    
    public Screen() {
    }

    public Screen(String name) {
        super();
        put("name", name);
    }
    
    @JsonAnyGetter
    public Map<String, Object> any() {
       return this.additionalProperties;
    }

    @JsonAnySetter
    @Override
    public void put(String name, Object value) {
       this.additionalProperties.put(name, value);
    }
    
    @Override
    public Object get(String name) {
        return additionalProperties.get(name);
    }
    
    @Override
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void setType(String type) {
        this.type = type;
    }
    
    @Override
    public String getType() {
        return type;
    }

    @SuppressWarnings("unchecked")
    @Override
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
    @Override
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

    public static class MenuAction {

        String text;
        String action;
        boolean enabled;

        public MenuAction(String text, String action, boolean enabled) {
            this.text = text;
            this.action = action;
            this.enabled = enabled;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((text == null) ? 0 : text.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            MenuAction other = (MenuAction) obj;
            if (text == null) {
                if (other.text != null)
                    return false;
            } else if (!text.equals(other.text))
                return false;
            return true;
        }
    }

}
