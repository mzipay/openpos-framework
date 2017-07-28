package org.jumpmind.jumppos.core.model;


public class FormButton implements IFormElement {
    
    private String elementType = "button";
    private String label;
    private String buttonAction;

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
