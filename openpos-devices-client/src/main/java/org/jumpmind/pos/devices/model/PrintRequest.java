package org.jumpmind.pos.devices.model;

public class PrintRequest extends DeviceRequest {

    private static final long serialVersionUID = 1L;

    PrintableDocument document;

    public PrintRequest(String profile, String deviceName, PrintableDocument document) {
        super(profile, deviceName);
        this.document = document;
    }

    public PrintRequest() {
    }

    public void setDocument(PrintableDocument document) {
        this.document = document;
    }

    public PrintableDocument getDocument() {
        return document;
    }

}
