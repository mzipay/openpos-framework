package org.jumpmind.jumppos.core.model;

import java.io.Serializable;
import java.util.ArrayList;
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
    
    public void addFormElement(IFormElement formElement) {
        formElements.add(formElement);
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
