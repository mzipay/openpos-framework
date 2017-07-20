package org.jumpmind.jumppos.model;

import java.util.Map;

public class Action {

    String name;

    Map<String, Map<String, Object>> data;

    public Action(String name) {
	super();
	this.name = name;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Map<String, Map<String, Object>> getData() {
	return data;
    }

    public void setData(Map<String, Map<String, Object>> data) {
	this.data = data;
    }

}
