package org.jumpmind.jumppos.core.model;


public interface IScreen {
    public static final String GLOBAL_NAV_ACTIONS_KEY = "navActions";
    public static final String MENU_ACTIONS_KEY = "menuActions";
    public static final String DIALOG_MESSAGE_LINE_KEY = "message";
    public static final String DIALOG_TITLE_KEY = "title";
    
    public static final String DIALOG_SCREEN_TYPE = "Dialog";
    public static final String SELL_SCREEN_TYPE = "Sell";
    public static final String PROMPT_SCREEN_TYPE = "Prompt";
    public static final String FORM_SCREEN_TYPE = "Form";
    public static final String SELL_ITEM_DETAIL_SCREEN_TYPE = "SellItemDetail";

    
    public void put(String name, Object value);

    public Object get(String name);

    public void setName(String name);

    public String getName();

    public void setType(String type);

    public String getType();

    public void addToGroup(String groupName, String dataName, Object value);

    public void addToList(String dataName, Object value);

}
