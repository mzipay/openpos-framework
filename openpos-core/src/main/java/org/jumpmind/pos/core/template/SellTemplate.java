package org.jumpmind.pos.core.template;

public class SellTemplate extends AbstractTemplate {

    private static final long serialVersionUID = 1L;
    
    boolean autoFocusOnScan = false;
    
    
    public SellTemplate() {
        super("Sell");
    }
    
    public void setAutoFocusOnScan(boolean autoFocusOnScan) {
        this.autoFocusOnScan = autoFocusOnScan;
    }
    
    public boolean isAutoFocusOnScan() {
        return autoFocusOnScan;
    }


}
