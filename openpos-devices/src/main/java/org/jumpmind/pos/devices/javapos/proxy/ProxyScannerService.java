package org.jumpmind.pos.devices.javapos.proxy;

import org.jumpmind.pos.devices.service.scan.ScanMessage;
import org.jumpmind.pos.devices.service.scan.ScannerConfigRequest;
import org.jumpmind.pos.service.ServiceResult;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import jpos.JposConst;
import jpos.JposException;
import jpos.events.DataEvent;
import jpos.services.EventCallbacks;
import jpos.services.ScannerService114;

public class ProxyScannerService extends AbstractBaseService implements ScannerService114 {

    private boolean autoDisable;
    private boolean dataEventEnabled;
    private boolean decodeData;
    private ScanMessage lastScanMessage;

    
    @Override
    public void open(String deviceName, EventCallbacks eventcallbacks) throws JposException {        
        super.open(deviceName, eventcallbacks);
        registerStompClient(deviceName, ScanMessage.class, (m)->{
            this.lastScanMessage = (ScanMessage)m;
            eventcallbacks.fireDataEvent(new DataEvent(this, JposConst.JPOS_SUCCESS));
        });
    }


    @Override
    public boolean getAutoDisable() throws JposException {
        return this.autoDisable;
    }

    @Override
    public void setAutoDisable(boolean autoDisable) throws JposException {
        this.autoDisable = autoDisable;
    }

    @Override
    public int getDataCount() throws JposException {
        return lastScanMessage != null ? lastScanMessage.getScanDataCount() : 0;
    }

    @Override
    public boolean getDataEventEnabled() throws JposException {
        return this.dataEventEnabled;
    }

    @Override
    public void setDataEventEnabled(boolean dataEventEnabled) throws JposException {
        this.dataEventEnabled = dataEventEnabled;
        RestTemplate restTemplate = getRestTemplate();
        ScannerConfigRequest req = new ScannerConfigRequest("dev", "Scanner", autoDisable, dataEventEnabled, decodeData);
        HttpEntity<ScannerConfigRequest> requestEntity = new HttpEntity<ScannerConfigRequest>(req);
        HttpEntity<ServiceResult> response = restTemplate.exchange(getBaseHttpUrl() + "/scan/config", HttpMethod.PUT, requestEntity,
                ServiceResult.class);

        ServiceResult result = response.getBody();
        processServiceResult(result);
    }

    @Override
    public boolean getDecodeData() throws JposException {
        return this.decodeData;
    }

    @Override
    public void setDecodeData(boolean decodeData) throws JposException {
        this.decodeData = decodeData;
    }

    @Override
    public byte[] getScanData() throws JposException {
        return lastScanMessage != null ? lastScanMessage.getScanData().getBytes() : null;
    }

    @Override
    public byte[] getScanDataLabel() throws JposException {
        return lastScanMessage != null ? lastScanMessage.getScanDataLabel().getBytes() : null;
    }

    @Override
    public int getScanDataType() throws JposException {
        return lastScanMessage != null ? lastScanMessage.getScanDataType() : 0;
    }

    @Override
    public void clearInput() throws JposException {
        // TODO make call
    }

    @Override
    public void reset() {
        // TODO make call
    }

    @Override
    public void clearInputProperties() throws JposException {
    }

    @Override
    public boolean getCapCompareFirmwareVersion() throws JposException {
        return false;
    }

    @Override
    public boolean getCapUpdateFirmware() throws JposException {
        return false;
    }

    @Override
    public void compareFirmwareVersion(String firmwareFileName, int[] result) throws JposException {
    }

    @Override
    public void updateFirmware(String firmwareFileName) throws JposException {
    }

    @Override
    public boolean getCapStatisticsReporting() throws JposException {
        return false;
    }

    @Override
    public boolean getCapUpdateStatistics() throws JposException {
        return false;
    }

    @Override
    public void resetStatistics(String statisticsBuffer) throws JposException {
    }

    @Override
    public void retrieveStatistics(String[] statisticsBuffer) throws JposException {
    }

    @Override
    public void updateStatistics(String statisticsBuffer) throws JposException {
    }

    @Override
    public int getCapPowerReporting() throws JposException {
        return 0;
    }

    @Override
    public int getPowerState() throws JposException {
        return 0;
    }
}
