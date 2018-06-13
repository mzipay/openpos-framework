package org.jumpmind.pos.core.screen;

import java.io.Serializable;

public class ScopeField implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private String value;
    private String date;
    private String stackTrace;
    
    public ScopeField(String name, String value, String date, String stackTrace) {
    	this.name = name;
    	this.value = value;
    	this.date = date;
    	this.stackTrace = stackTrace;
    }
    
    public String getName() {
    	return this.name;
    }
    
    public String getValue() {
    	return this.value;
    }
    
    public String getDate() {
    	return this.date;
    }
    
    public String getStackTrace() {
    	return this.stackTrace;
    }
}
