package org.jumpmind.pos.devices.model;

public class FileImage extends DocumentElement {

    int width;
    int alignment;
    String fileName;

    public FileImage() {
    }

    public FileImage(String fileName, int width, int alignment) {
        this.fileName = fileName;
        this.width = width;
        this.alignment = alignment;
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    public int getAlignment() {
        return alignment;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getFileName() {
        return fileName;
    }
}
