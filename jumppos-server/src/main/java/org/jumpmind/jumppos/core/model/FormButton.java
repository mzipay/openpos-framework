package org.jumpmind.jumppos.core.model;

import java.io.Serializable;

public class FormButton implements IFormElement, Serializable {
    private static final long serialVersionUID = 1L;
    
    private String elementType = "button";
    private String label;
    private String buttonAction;

    public FormButton() {
    }
    
    public FormButton(String label, String buttonAction) {
        this.label = label;
        this.buttonAction = buttonAction;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getButtonAction() {
        return buttonAction;
    }

    public void setButtonAction(String buttonAction) {
        this.buttonAction = buttonAction;
    }

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

}
