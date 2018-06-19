package org.jumpmind.pos.context.model;

public class TagModel {
    
    public static final String TAG_PREFIX = "TAG_";
    public static final String TAG_ALL = "*";
    public static final String TAG_CODE_TYPE = "CODE";
    
    private String name; // e.g. STORE
    private String group;  // e.g. LOCATION
    private int level;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    
}
