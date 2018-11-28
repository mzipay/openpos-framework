package org.jumpmind.pos.devices.service.print;

public class MemoryImage extends DocumentElement {
    
    byte[] data;
    int type;
    int width;
    int alignment;

    public MemoryImage() {
    }

    public MemoryImage(byte[] data, int type, int width, int alignment) {
        this.data = data;
        this.type = type;
        this.width = width;
        this.alignment = alignment;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
    
    
}
