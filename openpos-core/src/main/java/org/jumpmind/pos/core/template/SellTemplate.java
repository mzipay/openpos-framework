package org.jumpmind.pos.core.template;

import org.jumpmind.pos.core.template.Scan.ScanType;

public class SellTemplate extends AbstractTemplate {

    private static final long serialVersionUID = 1L;

    StatusBar statusBar = new StatusBar();

    Scan scan;

    public SellTemplate() {
        super("Sell");
    }

    public void setStatusBar(StatusBar statusBar) {
        this.statusBar = statusBar;
    }

    public StatusBar getStatusBar() {
        return statusBar;
    }

    public void setScan(Scan scan) {
        this.scan = scan;
    }

    public Scan getScan() {
        return scan;
    }

    public SellTemplate enableScan(boolean autoFocusOnScan) {
        Scan scan = new Scan();
        scan.setAutoFocusOnScan(autoFocusOnScan);
        scan.setScanType(ScanType.CAMERA_CORDOVA);
        scan.setScanActionName("Scan");
        setScan(scan);
        return this;
    }

}
