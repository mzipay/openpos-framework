package org.jumpmind.pos.devices.service.print;

import org.jumpmind.pos.devices.service.DeviceRequest;

public class PrintRequest extends DeviceRequest {

    private static final long serialVersionUID = 1L;
    
    PrintableDocument document;
    
    public void setDocument(PrintableDocument document) {
        this.document = document;
    }
    
    public PrintableDocument getDocument() {
        return document;
    }
    
}
