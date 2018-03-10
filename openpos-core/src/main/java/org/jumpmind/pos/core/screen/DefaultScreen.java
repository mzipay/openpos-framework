package org.jumpmind.pos.core.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultScreen extends AbstractScreen {

    private static final long serialVersionUID = 1L;

    public enum ScanType { CAMERA_CORDOVA, NONE }
    
    public static String TEMPLATE_SELL = "Sell";
    public static String TEMPLATE_BLANK = "Blank";

    private String prompt;
    private Workstation workstation;
    private String operatorName;
    private String userDisplayName = "Jane Doe";
    private boolean refreshAlways = false;
    private String theme = "openpos-theme";
    private String icon;  
    private String instructions;
    private String locale;
    private boolean useOnScreenKeyboard = false;
    
    private List<MenuItem> localMenuItems = new ArrayList<>();
    
    private boolean showScan = false;
    private boolean isReadOnly = false;
    private String scanActionName = "Scan";
    private ScanType scanType = ScanType.NONE;

    public DefaultScreen() {
    }

    public DefaultScreen(String type) {
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

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public void setRefreshAlways(boolean refreshAlways) {
        this.refreshAlways = refreshAlways;
    }
    
    public boolean isRefreshAlways() {
        return refreshAlways;
    }

    public static <T> T convertActionData(Object actionData, Class<T> convertToInstanceOf) {
        return new ObjectMapper().convertValue(actionData, convertToInstanceOf);
    }
    
    public String getTheme() {
        return theme;
    }
    
    public void setTheme(String theme) {
        this.theme = theme;
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

    public ScanType getScanType() {
        return scanType;
    }

    public void setScanType(ScanType scanType) {
        this.scanType = scanType;
    }

    public void setScanActionName(String scanActionName) {
        this.scanActionName = scanActionName;
    }

    public String getScanActionName() {
        return scanActionName;
    }
    
    public void setShowScan(boolean showScan) {
        this.showScan = showScan;
    }
    
    public boolean isShowScan() {
        return showScan;
    }
    
	public void setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	public boolean isReadOnly() {
		return isReadOnly;
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
        this.instructions = instructions;
    }
    
    public String getInstructions() {
        return instructions;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

	public boolean isUseOnScreenKeyboard() {
		return useOnScreenKeyboard;
	}

	public void setUseOnScreenKeyboard(boolean useOnScreenKeyboard) {
		this.useOnScreenKeyboard = useOnScreenKeyboard;
	}
}
