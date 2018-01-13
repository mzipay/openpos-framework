package org.jumpmind.pos.core.model;

import java.io.Serializable;


public class MaskElement implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum MaskElementType {
        String,
        RegExp
    }
    
    public static final MaskElement DIGIT_ELEMENT = new MaskElement(MaskElementType.RegExp, "\\d");
    
    private MaskElementType type;
    private String value;
    
    public MaskElement(MaskElementType type, String value) {
        this.type = type;
        this.value = value;
    }

    public MaskElementType getType() {
        return type;
    }

    public void setType(MaskElementType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
