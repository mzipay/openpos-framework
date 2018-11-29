package org.jumpmind.pos.devices.service.print;

import org.jumpmind.pos.devices.service.DeviceRequest;

public class PrinterSettingsRequest extends DeviceRequest {

    private static final long serialVersionUID = 1L;

    POSPrinterSettings settings;
    
    public PrinterSettingsRequest(String profile, String deviceName, POSPrinterSettings settings) {
        super(profile, deviceName);
        this.settings = settings;
    }
    
    public PrinterSettingsRequest() {
    }
    
    public void setSettings(POSPrinterSettings settings) {
        this.settings = settings;
    }
    
    public POSPrinterSettings getSettings() {
        return settings;
    }
}
