package org.jumpmind.pos.devices.service.print;

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
