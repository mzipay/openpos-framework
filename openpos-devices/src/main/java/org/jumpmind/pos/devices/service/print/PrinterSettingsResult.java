package org.jumpmind.pos.devices.service.print;

import org.jumpmind.pos.service.ServiceResult;

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
