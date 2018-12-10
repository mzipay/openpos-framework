package org.jumpmind.pos.devices.model;

import org.jumpmind.pos.util.model.ServiceResult;

public class PrinterSettingsResult extends ServiceResult {

    private static final long serialVersionUID = 1L;

    POSPrinterSettings settings;
    
    public PrinterSettingsResult(POSPrinterSettings settings) {
        this.settings = settings;
    }
    
    public PrinterSettingsResult() {
    }
    
    public void setSettings(POSPrinterSettings settings) {
        this.settings = settings;
    }
    
    public POSPrinterSettings getSettings() {
        return settings;
    }
}
