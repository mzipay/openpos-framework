package org.jumpmind.pos.devices.model;

import java.util.List;

public class PrintableDocument {

    List<DocumentElement> elements;
    
    public List<DocumentElement> getElements() {
        return elements;
    }
    
    public void setElements(List<DocumentElement> elements) {
        this.elements = elements;
    }
    
}
