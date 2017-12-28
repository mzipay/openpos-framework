package org.jumpmind.pos.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Form implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private List<IFormElement> formElements = new ArrayList<IFormElement>();
    
    private boolean requiresAtLeastOneValue = false;
    
    private String name;

    public List<IFormElement> getFormElements() {
        return formElements;
    }

    public void setFormElements(List<IFormElement> formElements) {
        this.formElements = formElements;
    }
    
    public IFormElement addFormElement(IFormElement formElement) {
        formElements.add(formElement);
        return formElement;
    }
    
    public ComboField addComboBox(String fieldId, String label, String... values) {
        return addComboBox(fieldId, label, values != null && values.length > 0 ? Arrays.asList(values) : new ArrayList<>());
    }
    
    public ComboField addComboBox(String fieldId, String label, List<String> values) {
        ComboField field = new ComboField(fieldId, label, null, values);
        formElements.add(field);
        return field;        

    }
    
    public FormField addMoneyField(String fieldId, String label, String value, boolean required) {
        FormField formField = new FormField(fieldId, label, FieldElementType.Input, FieldInputType.NumericText, true);
        formField.setPattern("^(\\d{0,9}\\.\\d{0,2}|\\d{1,9})$");
        formField.setValue(value);
        formElements.add(formField);
        return formField;
    }
    
    public FormField addPercentField(String fieldId, String label, String value, boolean required) {
        FormField formField = new FormField(fieldId, label, FieldElementType.Input, FieldInputType.NumericText, true);
        formField.setPattern("\\d{1,2}(?!\\d)|100");
        formField.setValue(value);
        formElements.add(formField);
        return formField;
    }
    
    public void addSeparator() {
        formElements.add(new FormDisplayField(FieldElementType.Separator));
    }

    public void removeFormElement(IFormElement formElement) {
        formElements.remove(formElement);
    }
    
    public void removeFormElement(String elementId) {
        IFormElement elementToRemove = this.getFormElement(elementId);
        if (null != elementToRemove) {
            formElements.remove(elementToRemove);
        }
    }
        
    public IFormElement getFormElement(String elementId) {
        return formElements.stream().filter(f->elementId.equals(f.getId())).findFirst().orElse(null);
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setRequiresAtLeastOneValue(boolean requiresAtLeastOneValue) {
        this.requiresAtLeastOneValue = requiresAtLeastOneValue;
    }
    
    public boolean isRequiresAtLeastOneValue() {
        return requiresAtLeastOneValue;
    }
    
    public String getString(String id) {
        for (IFormElement element : formElements) {
            if (element.getId().equals(id)) {
                if (element instanceof FormField) {
                    return ((FormField)element).getValue();                            
                }
            }
        }
        return null;
        
    }
    
}
