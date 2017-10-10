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
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
}
