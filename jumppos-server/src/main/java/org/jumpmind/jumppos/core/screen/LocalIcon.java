package org.jumpmind.jumppos.core.screen;

/**
 * Indicates that the icon is hosted locally on the client and should be resolved
 * in the context of the client UI.
 */
public class LocalIcon implements IIcon {

    private String name;
    
    public LocalIcon(String name) {
        this.name = String.format("local_%s", name);
    }
    
    @Override
    public String getName() {
        return name;
    }

}
