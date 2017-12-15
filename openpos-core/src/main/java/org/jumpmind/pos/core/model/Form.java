package org.jumpmind.pos.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Form implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private List<IFormElement> formElements = new ArrayList<IFormElement>();
    
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
    
    public FormListField addComboBox(String fieldId, String label, String... values) {
        FormListField field = new FormListField(fieldId, label, FieldElementType.Input, FieldInputType.ComboBox, null, values != null && values.length > 0 ? Arrays.asList(values) : new ArrayList<>());
        formElements.add(field);
        return field;
        
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
