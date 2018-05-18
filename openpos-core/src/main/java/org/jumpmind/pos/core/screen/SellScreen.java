package org.jumpmind.pos.core.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellScreen extends AbstractScreen {

    private static final long serialVersionUID = 1L;
    

    
    public SellScreen() {
    }

    public SellScreen(String type) {
        super(null, type);
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
    
    /**
     * Indicator for marking the screen as "Customer Facing", meaning that the screen is intended for the customer to 
     * complete.
     * @param customerFacing <code>true</code> if the customer should use the screen.
     */
    public void setCustomerFacing(Boolean customerFacing) {
        put("customerFacing", customerFacing);
    }        
    
    public void setInstructions(String instructions) {
        this.put("instructions", instructions);
    }
    
}
