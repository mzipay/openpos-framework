package org.jumpmind.pos.persist.impl;

public class DmlTemplate {

    String name;
    String dml;
    
    public void setDml(String dml) {
        this.dml = dml;
    }
    
    public String getDml() {
        return dml;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
}
