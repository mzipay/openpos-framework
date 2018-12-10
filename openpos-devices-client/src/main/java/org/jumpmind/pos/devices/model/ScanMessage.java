package org.jumpmind.pos.devices.model;

import org.jumpmind.pos.util.model.Message;

public class ScanMessage extends Message {

    private static final long serialVersionUID = 1L;

    String scanData;
    String scanDataLabel;
    int scanDataType;
    boolean autoDisable;
    boolean dataEventEnabled;
    boolean deviceEnabled;
    boolean freezeEvents;
    boolean decodeData;
    int scanDataCount;
    
    public ScanMessage() {
        setType("Devices");
    }

    public ScanMessage(String scanData, String scanDataLabel, int scanDataType, boolean autoDisable, boolean dataEventEnabled,
            boolean deviceEnabled, boolean freezeEvents, boolean decodeData, int scanDataCount) {
        this();
        this.scanData = scanData;
        this.scanDataLabel = scanDataLabel;
        this.scanDataType = scanDataType;
        this.autoDisable = autoDisable;
        this.dataEventEnabled = dataEventEnabled;
        this.deviceEnabled = deviceEnabled;
        this.freezeEvents = freezeEvents;
        this.decodeData = decodeData;
        this.scanDataCount = scanDataCount;
    }

    public String getScanData() {
        return scanData;
    }

    public void setScanData(String scanData) {
        this.scanData = scanData;
    }

    public String getScanDataLabel() {
        return scanDataLabel;
    }

    public void setScanDataLabel(String scanDataLabel) {
        this.scanDataLabel = scanDataLabel;
    }

    public int getScanDataType() {
        return scanDataType;
    }

    public void setScanDataType(int scanDataType) {
        this.scanDataType = scanDataType;
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

    public boolean isDeviceEnabled() {
        return deviceEnabled;
    }

    public void setDeviceEnabled(boolean deviceEnabled) {
        this.deviceEnabled = deviceEnabled;
    }

    public boolean isFreezeEvents() {
        return freezeEvents;
    }

    public void setFreezeEvents(boolean freezeEvents) {
        this.freezeEvents = freezeEvents;
    }

    public boolean isDecodeData() {
        return decodeData;
    }

    public void setDecodeData(boolean decodeData) {
        this.decodeData = decodeData;
    }

    public int getScanDataCount() {
        return scanDataCount;
    }

    public void setScanDataCount(int scanDataCount) {
        this.scanDataCount = scanDataCount;
    }

}
