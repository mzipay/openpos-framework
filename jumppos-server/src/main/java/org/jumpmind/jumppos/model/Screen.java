package org.jumpmind.jumppos.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Screen extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    public Screen() {
    }

    public Screen(String name) {
        super();
        put("name", name);
    }

    public String getName() {
        return (String) get("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getType() {
        return (String) get("type");
    }

    public void setType(String type) {
        put("type", type);
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
