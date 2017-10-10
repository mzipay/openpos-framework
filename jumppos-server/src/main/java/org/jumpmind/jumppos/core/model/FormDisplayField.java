package org.jumpmind.jumppos.core.model;

import java.io.Serializable;

public class FormDisplayField implements IFormElement, Serializable {
    private static final long serialVersionUID = 1L;
    
    private String elementType = "display";
    private String label;
    private String fieldId;
    private String value;

    public FormDisplayField() {
    }

    public FormDisplayField(String label, String fieldId, String value) {
        this.label = label;
        this.fieldId = fieldId;
        this.value = value;
    }

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
