package org.jumpmind.jumppos.core.model;


public class FormField implements IFormElement {
    
    private String elementType = "input";
    private String inputType = "text";
    private String label;
    private String fieldId;
    private String value;
    private String placeholder;
    
    public String getInputType() {
        return inputType;
    }
    public void setInputType(String inputType) {
        this.inputType = inputType;
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
    public String getElementType() {
        return elementType;
    }
    public void setElementType(String elementType) {
        this.elementType = elementType;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getPlaceholder() {
        return placeholder;
    }
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

}
