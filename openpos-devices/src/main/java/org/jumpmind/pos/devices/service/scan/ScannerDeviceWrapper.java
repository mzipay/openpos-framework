package org.jumpmind.pos.devices.service.scan;

import org.jumpmind.pos.devices.model.ScanMessage;
import org.jumpmind.pos.devices.service.AbstractDeviceWrapper;
import org.jumpmind.pos.devices.service.DeviceRequest;
import org.jumpmind.pos.service.ServiceResult.Result;
import org.springframework.stereotype.Component;

import jpos.JposConst;
import jpos.JposException;
import jpos.Scanner;
import jpos.events.DataEvent;
import jpos.events.DataListener;

@Component
public class ScannerDeviceWrapper extends AbstractDeviceWrapper<Scanner, ScannerStatusResult> {

    public ScannerStatusResult activate(ScannerActivateRequest req) {
        ScannerStatusResult result = doSynchronized((r) -> {
            Scanner scanner = getDevice(req);
            setMode(req, scanner);
            r.setResultStatus(Result.SUCCESS);
            r.setMode(scanner.getAutoDisable() ? ScannerMode.SINGLE_SCAN : ScannerMode.MULTI_SCAN);
        }, req, ScannerStatusResult.class);
        return result;
    }
    public ScannerStatusResult deactivate(DeviceRequest req) {
        ScannerStatusResult result = doSynchronized((r) -> {
            Scanner scanner = getDevice(req);
            scanner.setDeviceEnabled(false); 
            r.setResultStatus(Result.SUCCESS);
            r.setMode(scanner.getAutoDisable() ? ScannerMode.SINGLE_SCAN : ScannerMode.MULTI_SCAN);
        }, req, ScannerStatusResult.class);
        return result;
    }

    @Override
    protected Scanner create(DeviceRequest req) throws JposException {
        Scanner scanner = new Scanner();
        scanner.addDataListener(new ScannerDataListener(req.getDeviceName()));
        setMode((ScannerActivateRequest) req, scanner);
        return scanner;
    }

    protected void setMode(ScannerActivateRequest req, Scanner scanner) throws JposException {
        if (scanner.getState() == JposConst.JPOS_S_CLOSED) {
            scanner.open(req.getDeviceName());
        }

        if (!scanner.getClaimed()) {
            scanner.claim(1000);
        }

        if (!scanner.getDeviceEnabled()) {
            scanner.setDeviceEnabled(true);
        }

        if (!scanner.getDecodeData()) {
            scanner.setDecodeData(true);
        }

        scanner.setAutoDisable(req.getMode() == ScannerMode.SINGLE_SCAN);
        scanner.getDataCount();
        scanner.setDataEventEnabled(true);

        scanner.setDeviceEnabled(true);
    }

    class ScannerDataListener implements DataListener {
        
        String logicalName;
        
        public ScannerDataListener(String logicalName) {
            this.logicalName = logicalName;
        }
        
        
        @Override
        public void dataOccurred(DataEvent e) {
            try {
                Scanner scanner = (Scanner) e.getSource();
                String scanData = new String(scanner.getScanData());
                String scanDataLabel = new String(scanner.getScanDataLabel());
                int scanDataType = scanner.getScanDataType();
                boolean autoDisable = scanner.getAutoDisable();
                boolean dataEventEnabled = scanner.getDataEventEnabled();
                boolean deviceEnabled = scanner.getDeviceEnabled();
                boolean freezeEvents = scanner.getFreezeEvents();
                boolean decodeData = scanner.getDecodeData();
                int scanDataCount = scanner.getDataCount();
                scanner.clearInput();
                scanner.clearInputProperties();
                scanner.getState();
                messageService.sendMessage("Devices", logicalName, new ScanMessage(scanData, scanDataLabel, scanDataType, autoDisable, dataEventEnabled, deviceEnabled, freezeEvents, decodeData, scanDataCount));
            } catch (JposException e1) {
                logger.error("", e1);
            }
        }
    }

}
