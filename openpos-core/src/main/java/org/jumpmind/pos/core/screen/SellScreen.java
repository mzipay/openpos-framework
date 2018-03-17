package org.jumpmind.pos.core.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SellScreen extends AbstractScreen {

    private static final long serialVersionUID = 1L;

    public enum ScanType { CAMERA_CORDOVA, NONE }
    
    private String prompt;
    private Workstation workstation;
    private String operatorName;
    private String icon;
    
    private List<MenuItem> localMenuItems = new ArrayList<>();
    
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
    
    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Workstation getWorkstation() {
        return workstation;
    }

    public void setWorkstation(Workstation workstation) {
        this.workstation = workstation;
    }

    public static <T> T convertActionData(Object actionData, Class<T> convertToInstanceOf) {
        return new ObjectMapper().convertValue(actionData, convertToInstanceOf);
    }
        
    public MenuItem getLocalMenuItemByAction(String action) {
        return this. localMenuItems.stream().filter( mi -> action.equalsIgnoreCase(mi.getAction())).findFirst().orElse(null);
    }
    
    public MenuItem getLocalMenuItemByTitle(String title) {
        return this.localMenuItems.stream().filter( mi -> title.equalsIgnoreCase(mi.getTitle())).findFirst().orElse(null);
    }

    public void addLocalMenuItem(MenuItem menuItem) {
        this.localMenuItems.add(menuItem);
    }
    
    public void setLocalMenuItems(List<MenuItem> localMenuItems) {
        this.localMenuItems = localMenuItems;
    }
    
    public List<MenuItem> getLocalMenuItems() {
        return localMenuItems;
    }

    public void setScanType(ScanType scanType) {
        put("scanType", scanType);
    }

    public void setScanActionName(String scanActionName) {
        put("scanActionName", scanActionName);
    }

    /**
     * Indicator for marking the screen as "Customer Facing", meaning that the screen is intended for the customer to 
     * complete.
     * @param customerFacing <code>true</code> if the customer should use the screen.
     */
    public void setCustomerFacing(Boolean customerFacing) {
        put("customerFacing", customerFacing);
    }
    
    public void setShowScan(boolean showScan) {
        put("showScan", showScan);
    }
    
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
    
    public String getPrompt() {
        return prompt;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public void setInstructions(String instructions) {
        this.put("instructions", instructions);
    }
    
}
