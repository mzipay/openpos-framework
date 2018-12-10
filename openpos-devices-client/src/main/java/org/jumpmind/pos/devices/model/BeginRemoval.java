package org.jumpmind.pos.devices.model;

public class BeginRemoval extends DocumentElement {

    int timeout;
    
    public BeginRemoval() {
    }
    
    public BeginRemoval(int timeout) {
        this.timeout = timeout;
    }
    
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    
    public int getTimeout() {
        return timeout;
    }

    
}
