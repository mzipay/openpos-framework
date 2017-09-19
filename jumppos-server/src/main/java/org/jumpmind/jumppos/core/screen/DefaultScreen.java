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

    public static final String EMBEDDED_WEB_PAGE = "EmbeddedWebPage";
    public static final String CHOOSE_OPTIONS_SCREEN_TYPE = "ChooseOptions";
    public static final String PROMPT_WITH_OPTIONS_SCREEN_TYPE = "PromptWithOptions";
    public static final String DIALOG_SCREEN_TYPE = "Dialog";
    public static final String FORM_SCREEN_TYPE = "Form";
    public static final String PAYMENT_STATUS = "PaymentStatus";
    public static final String PROMPT_SCREEN_TYPE = "Prompt";
    public static final String SELL_ITEM_DETAIL_SCREEN_TYPE = "SellItemDetail";
    public static final String SELL_SCREEN_TYPE = "Sell";
    public static final String SIGNATURE_CAPTURE_SCREEN_TYPE = "SignatureCapture";
    public static final String SHOW_CART_SCREEN = "Cart";

    public static final String TITLE_OPEN_STATUS = "Open";
    public static final String TITLE_CLOSED_STATUS = "Closed";

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private String name;
    private String type;
    private MenuItem backButton;
    private Workstation workstation;
    private String operatorName;
    private MenuItem storeStatus;
    private MenuItem registerStatus;
    private String userDisplayName = "Jane Doe";
    private boolean showStatusBar = true;
    private int sequenceNumber;
    private boolean refreshAlways = false;

    private List<MenuItem> menuItems = new ArrayList<>();

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
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItem) {
        this.menuItems = menuItem;
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

    public MenuItem getStoreStatus() {
        return storeStatus;
    }

    public void setStoreStatus(MenuItem storeStatus) {
        this.storeStatus = storeStatus;
    }

    public MenuItem getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(MenuItem registerStatus) {
        this.registerStatus = registerStatus;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public boolean isShowStatusBar() {
        return showStatusBar;
    }

    public void setShowStatusBar(boolean showStatusBar) {
        this.showStatusBar = showStatusBar;
    }
    
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
    public int getSequenceNumber() {
        return sequenceNumber;
    }
    
    public void setRefreshAlways(boolean refreshAlways) {
        this.refreshAlways = refreshAlways;
    }
    
    public boolean isRefreshAlways() {
        return refreshAlways;
    }

}
