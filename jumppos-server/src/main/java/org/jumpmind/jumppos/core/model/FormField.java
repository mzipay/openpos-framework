package org.jumpmind.jumppos.core.model;

import java.io.Serializable;

public class FormField implements IFormElement, IField, Serializable {
    private static final long serialVersionUID = 1L;
    
    
    private FieldElementType elementType = FieldElementType.Input;
    private FieldInputType inputType = FieldInputType.AlphanumericText;
    private String label;
    private String fieldId;
    private String value;
    private String placeholder;
    private String pattern;
    private boolean required = true;

    public FormField() {
    }
    
    public FormField(String fieldId, String placeholder) {
        this.fieldId = fieldId;
        this.placeholder = placeholder;
    }

    public FormField(String fieldId, String label, String placeholder) {
        this.fieldId = fieldId;
        this.label = label;
        this.placeholder = placeholder;
    }
    
    public FormField(String fieldId, String label, FieldElementType elementType, FieldInputType inputType, String placeholder) {
        this.fieldId = fieldId;
        this.label = label;
        this.elementType = elementType;
        this.inputType = inputType;
        this.placeholder = placeholder;
    }
    
    public FormField(String fieldId, String label, FieldElementType elementType, FieldInputType inputType, boolean required) {
        this.fieldId = fieldId;
        this.label = label;
        this.elementType = elementType;
        this.inputType = inputType;
        this.required = required;
    }
    
    public FormField(String fieldId, String label, FieldElementType elementType, FieldInputType inputType, boolean required, String value) {
        this.fieldId = fieldId;
        this.label = label;
        this.elementType = elementType;
        this.inputType = inputType;
        this.required = required;
        this.value = value;
    }    
    
    public FieldInputType getInputType() {
        return inputType;
    }

    public void setInputType(FieldInputType inputType) {
        this.inputType = inputType;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String getId() {
        return fieldId;
    }

    @Override
    public void setId(String fieldId) {
        this.fieldId = fieldId;
    }

    
    public FieldElementType getElementType() {
        return elementType;
    }

    public void setElementType(FieldElementType elementType) {
        this.elementType = elementType;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }


}
