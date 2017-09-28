package org.jumpmind.jumppos.core.screen;

public class DefaultIcon implements IIcon {

    private String name;
    
    public DefaultIcon(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }

}
