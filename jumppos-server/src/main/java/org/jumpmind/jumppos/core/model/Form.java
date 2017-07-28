package org.jumpmind.jumppos.core.model;

import java.util.ArrayList;
import java.util.List;

public class Form {
    
    private List<IFormElement> formElements = new ArrayList<IFormElement>();

    public List<IFormElement> getFormElements() {
        return formElements;
    }

    public void setFormElements(List<IFormElement> formElements) {
        this.formElements = formElements;
    }
    
    public void addFormElement(IFormElement formElement) {
        formElements.add(formElement);
    }
    
}
