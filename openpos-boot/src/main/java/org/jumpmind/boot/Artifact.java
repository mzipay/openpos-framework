package org.jumpmind.boot;

import org.json.JSONObject;

public class Artifact {

    JSONObject json;
    
    public Artifact(JSONObject json) {
        this.json = json;
    }
    
    public String getName() {
        return this.json.getString("name");
    }
    
    public boolean isUpgradable() {
        return this.json.has("upgradeable") && this.json.getBoolean("upgradeable");
    }
    
    public boolean isIncludeInClasspath() {        
        return this.json.has("includeInClasspath") && this.json.getBoolean("includeInClasspath");
    }
    
    public boolean isOverwrite() {
        return json.has("overwrite") && json.getBoolean("overwrite");
    }
}
