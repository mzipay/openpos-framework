package org.jumpmind.jumppos.core.screen;

import java.io.Serializable;

public class DefaultIcon implements IIcon, Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    
    public DefaultIcon(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }

}
