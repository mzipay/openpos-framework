package org.jumpmind.jumppos.core.model;


public interface IScreen {

    public void put(String name, Object value);

    public Object get(String name);

    public void setName(String name);

    public String getName();

    public void setType(String type);

    public String getType();

    public void addToGroup(String groupName, String dataName, Object value);

    public void addToList(String dataName, Object value);

}
