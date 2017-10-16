package org.jumpmind.jumppos.core.screen;

import java.io.Serializable;

public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public Customer() {
    }
    
    public Customer(String name) {
        super();
        this.name = name;
    }

    String name;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

}
