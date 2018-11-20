package org.jumpmind.pos.devices.service;

import org.jumpmind.pos.devices.model.PrintableDocument;

public class PrintRequest {

    PrintableDocument document;
    
    public void setDocument(PrintableDocument document) {
        this.document = document;
    }
    
    public PrintableDocument getDocument() {
        return document;
    }
    
}
