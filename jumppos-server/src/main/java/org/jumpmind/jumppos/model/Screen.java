package org.jumpmind.jumppos.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Screen {

    String name;

    String type;

    List<MenuAction> menuActions = new ArrayList<>();
    List<MenuAction> navActions = new ArrayList<>();

    Map<String, Map<String, Object>> data = new HashMap<>();

    public Screen() {
    }

    public Screen(String name) {
	super();
	this.name = name;
    }

    public Screen(String name, String type, Map<String, Map<String, Object>> data) {
	this.name = name;
	this.type = type;
	this.data = data;
    }

    public List<MenuAction> getMenuActions() {
	return menuActions;
    }

    public void setMenuActions(List<MenuAction> menuActions) {
	this.menuActions = menuActions;
    }

    public List<MenuAction> getNavActions() {
	return navActions;
    }

    public void setNavActions(List<MenuAction> navActions) {
	this.navActions = navActions;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public Map<String, Map<String, Object>> getData() {
	return data;
    }

    public void setData(Map<String, Map<String, Object>> data) {
	this.data = data;
    }

    public void addData(String groupName, String dataName, Object value) {
	Map<String, Object> map = data.get(groupName);
	if (map == null) {
	    map = new HashMap<>();
	    data.put(groupName, map);
	}
	map.put(dataName, value);
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
    }

}
