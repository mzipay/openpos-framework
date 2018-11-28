package org.jumpmind.pos.devices.service.print;

import java.util.ArrayList;
import java.util.List;

public class PrintableDocument {

    int station;
    
    List<DocumentElement> elements = new ArrayList<>();
    
    public PrintableDocument(int station) {
        this.station = station;
    }
    
    public PrintableDocument() {
    }
    
    public List<DocumentElement> getElements() {
        return elements;
    }
    
    public void setElements(List<DocumentElement> elements) {
        this.elements = elements;
    }
    
    public void addElement(DocumentElement element) {
        this.elements.add(element);
    }
    
    public int getStation() {
        return station;
    }
    
    public void setStation(int station) {
        this.station = station;
    }
    
}
