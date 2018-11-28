package org.jumpmind.pos.devices.service.print;

import java.io.Serializable;

public class Barcode extends DocumentElement implements Serializable {

    private static final long serialVersionUID = 1L;

    int symbology;
    int height;
    int width;
    int alignment;
    int textPosition;
    String data;

    public Barcode() {
    }
    
    public Barcode(String data, int symbology, int height, int width, int alignment, int textPosition) {
        this.data = data;
        this.symbology = symbology;
        this.height = height;
        this.width = width;
        this.alignment = alignment;
        this.textPosition = textPosition;
    }
    
    public void setData(String data) {
        this.data = data;
    }
    
    public String getData() {
        return data;
    }

    public int getSymbology() {
        return symbology;
    }

    public void setSymbology(int symbology) {
        this.symbology = symbology;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getAlignment() {
        return alignment;
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    public int getTextPosition() {
        return textPosition;
    }

    public void setTextPosition(int textPosition) {
        this.textPosition = textPosition;
    }

}
