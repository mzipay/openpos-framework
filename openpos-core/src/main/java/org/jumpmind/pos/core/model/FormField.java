package org.jumpmind.pos.core.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public class FormField implements IFormElement, IField, Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * Put properties in this map if they are optional. When not set, they don't
     * show up in the json which means less overhead.
     */
    private Map<String, Object> optionalProperties = new HashMap<String, Object>();    
    
    private FieldElementType elementType = FieldElementType.Input;
    private FieldInputType inputType = FieldInputType.AlphanumericText;
    private String label;
    private String fieldId;
    private String value;
    private boolean required = true;
    private boolean sensitive = false;

    public FormField() {
    }
    
    public FormField(String fieldId, String placeholder) {
        this.fieldId = fieldId;
        setPlaceholder(placeholder);
    }

    public FormField(String fieldId, String label, String placeholder) {
        this.fieldId = fieldId;
        this.label = label;
        setPlaceholder(placeholder);
    }
    
    public FormField(String fieldId, String label, FieldElementType elementType, FieldInputType inputType, String placeholder) {
        this.fieldId = fieldId;
        this.label = label;
        this.elementType = elementType;
        this.inputType = inputType;
        setPlaceholder(placeholder);
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
        setIconName(iconName);
    } 
    
    @JsonAnyGetter
    public Map<String, Object> any() {
        return this.optionalProperties;
    }

    @JsonAnySetter
    public void put(String name, Object value) {
        this.optionalProperties.put(name, value);
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
    
    public void setPlaceholder(String placeholder) {
        this.optionalProperties.put("placeholder", placeholder);
    }

    public FormField placeholder(String placeholder) {
        this.setPlaceholder(placeholder);
        return this;
    }
    
    public void setPattern(String pattern) {
        this.put("pattern", pattern);
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

    public void setDisabled(boolean disabled) {
        put("disabled", disabled);
    }

    public FormField disabled(boolean disabled) {
        this.setDisabled(disabled);
        return this;
    }

    public void setMinLength(Integer minLength) {
        this.put("minLength", minLength);
    }

    public FormField minLength(Integer minLength) {
        this.setMinLength(minLength);
        return this;
    }
    
    public void setMaxLength(Integer maxLength) {
        this.put("maxLength", maxLength);
    }

    public FormField maxLength(Integer maxLength) {
        this.setMaxLength(maxLength);
        return this;
    }
    
    /**
     * When this value is set, the client will call back upon the selected value changing with an action whose name is the same 
     * as the one given
     * @param valueChangedAction The name of an action to generate when the Combo box selected value changes.
     */
    public void setValueChangedAction(String valueChangedAction) {
        this.put("valueChangedAction", valueChangedAction);
    }
    
    public FormField valueChangedAction(String valueChangedAction) {
        this.setValueChangedAction(valueChangedAction);
        return this;
    }
    
    public void setIconName(String iconName) {
    		this.put("iconName", iconName);
    }

    /**
     * Controls whether or not the text in the field should be selected on a click in the UI
     */
    public void setSelectText(boolean selectText) {
        this.put("select", selectText);
    }

    public FormField selectText(boolean selectText) {
        this.setSelectText(selectText);
        return this;
    }

    public boolean isSensitive() {
        return sensitive;
    }

    public void setSensitive(boolean sensitive) {
        this.sensitive = sensitive;
    }

    public FormField keyboardPreference(KeyboardType keyboardPreference) {
        this.setKeyboardPreference(keyboardPreference);
        return this;
    }

    public void setKeyboardPreference(KeyboardType keyboardPreference) {
        this.put("keyboardPreference", keyboardPreference);
    }

    public FormField scanEnabled(Boolean scanEnabled) {
        this.setScanEnabled(scanEnabled);
        return this;
    }

    public void setScanEnabled(Boolean scanEnabled) {
        this.put("scanEnabled", scanEnabled);
    }

    public void setValidators(Set<Validator> validators) {
        this.put("validators", validators);
    }
    
    public FormField addValidators(Validator ...validators) {
        if (validators != null && validators.length > 0) {
            if (! this.optionalProperties.containsKey("validators")) {
                this.put("validators", new HashSet<Validator>());
            }
            @SuppressWarnings("unchecked")
            Set<Validator> theValidators = (Set<Validator>) this.optionalProperties.get("validators");
            theValidators.addAll(Arrays.asList(validators));
        }
        return this;
    }

	public void setHintText(String hintText) {
		this.put("hintText", hintText);
	}
	
	public void setValidationMessage( Map<String,String> messages) {
		this.put("validationMessages", messages);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, String> getValidationMessages() {
		if(any().containsKey("validationMessages")) {
			return (Map<String, String>) any().get("validationMessages");
		}
		return null;
	}
	
    public void addValidationMessage( String validatorName, String message) {
    	if( getValidationMessages() == null) {
    		setValidationMessage(new HashMap<String, String>());
    	}
    	getValidationMessages().put(validatorName, message);
    }
}
