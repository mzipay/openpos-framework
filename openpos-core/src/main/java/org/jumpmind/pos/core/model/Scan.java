package org.jumpmind.pos.core.model;

import java.io.Serializable;

public class Scan implements Serializable {

    private static final long serialVersionUID = 1L;
    private String value;
    private String format;
    
    public Scan() {     
    }
    
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format;
    }
    
    
    
    
}
