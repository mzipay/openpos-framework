package org.jumpmind.pos.devices.model;

public class ScannerConfigRequest extends DeviceRequest {

    private static final long serialVersionUID = 1L;

    protected boolean autoDisable;
    protected boolean dataEventEnabled;
    protected boolean decodeData;

    public ScannerConfigRequest() {
    }

    public ScannerConfigRequest(String profile, String deviceName, boolean autoDisable, boolean dataEventEnabled, boolean decodeData) {        
        super(profile, deviceName);
        this.autoDisable = autoDisable;
        this.dataEventEnabled = dataEventEnabled;
        this.decodeData = decodeData;
    }

    public boolean isAutoDisable() {
        return autoDisable;
    }

    public void setAutoDisable(boolean autoDisable) {
        this.autoDisable = autoDisable;
    }

    public boolean isDataEventEnabled() {
        return dataEventEnabled;
    }

    public void setDataEventEnabled(boolean dataEventEnabled) {
        this.dataEventEnabled = dataEventEnabled;
    }

    public boolean isDecodeData() {
        return decodeData;
    }

    public void setDecodeData(boolean decodeData) {
        this.decodeData = decodeData;
    }

}
