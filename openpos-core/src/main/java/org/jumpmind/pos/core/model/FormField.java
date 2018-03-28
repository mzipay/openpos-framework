package org.jumpmind.pos.core.model;

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
    private boolean disabled = false;
    private String valueChangedAction;
    private String iconName;

    private Integer minLength;
    private Integer maxLength;

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
    
    public FormField(String fieldId, String label, FieldElementType elementType, FieldInputType inputType, boolean required, String value, String iconName) {
        this.fieldId = fieldId;
        this.label = label;
        this.elementType = elementType;
        this.inputType = inputType;
        this.required = required;
        this.value = value;
        this.iconName = iconName;
    } 
    
    public FieldInputType getInputType() {
        return inputType;
    }

    public void setInputType(FieldInputType inputType) {
        this.inputType = inputType;
    }

    public FormField inputType(FieldInputType inputType) {
        this.setInputType(inputType);
        return this;
    }
    
    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }
    
    public FormField label(String label) {
        this.setLabel(label);
        return this;
    }
    

    @Override
    public String getId() {
        return fieldId;
    }

    @Override
    public void setId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String id(String fieldId) {
        this.setId(fieldId);
        return this.fieldId;
    }
    
    public FieldElementType getElementType() {
        return elementType;
    }

    public void setElementType(FieldElementType elementType) {
        this.elementType = elementType;
    }

    public FormField elementType(FieldElementType elementType) {
        this.setElementType(elementType);
        return this;
    }
    
    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    public FormField value(String value) {
        this.setValue(value);
        return this;
    }
    
    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public FormField placeholder(String placeholder) {
        this.setPlaceholder(placeholder);
        return this;
    }
    
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public FormField pattern(String pattern) {
        this.setPattern(pattern);
        return this;
    }
    
    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public FormField required(boolean required) {
        this.setRequired(required);
        return this;
    }   

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public FormField disabled(boolean disabled) {
        this.setDisabled(disabled);
        return this;
    }
    
    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public FormField minLength(Integer minLength) {
        this.setMinLength(minLength);
        return this;
    }
    
    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public FormField maxLength(Integer maxLength) {
        this.setMaxLength(maxLength);
        return this;
    }
    
    public String getValueChangedAction() {
        return valueChangedAction;
    }

    /**
     * When this value is set, the client will call back upon the selected value changing with an action whose name is the same 
     * as the one given
     * @param valueChangedAction The name of an action to generate when the Combo box selected value changes.
     */
    public void setValueChangedAction(String valueChangedAction) {
        this.valueChangedAction = valueChangedAction;
    }
    
    public FormField valueChangedAction(String valueChangedAction) {
        this.setValueChangedAction(valueChangedAction);
        return this;
    }
    
    public String getIconName() {
    		return iconName;
    }
    
    public void setIconName(String iconName) {
    		this.iconName = iconName;
    }
   
}
