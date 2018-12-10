package org.jumpmind.pos.devices.model;

public class BeginInsert extends DocumentElement {

    int timeout;
    
    public BeginInsert() {
    }
    
    public BeginInsert(int timeout) {
        this.timeout = timeout;
    }
    
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    
    public int getTimeout() {
        return timeout;
    }
    
}
