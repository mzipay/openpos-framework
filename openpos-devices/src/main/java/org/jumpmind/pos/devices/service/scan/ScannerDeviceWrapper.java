package org.jumpmind.pos.devices.service.scan;

import org.jumpmind.pos.devices.DevicesUtils;
import org.jumpmind.pos.devices.service.AbstractDeviceWrapper;
import org.jumpmind.pos.devices.service.DeviceRequest;
import org.jumpmind.pos.service.ServiceResult;
import org.jumpmind.pos.service.ServiceResult.Result;
import org.springframework.stereotype.Component;

import jpos.JposConst;
import jpos.JposException;
import jpos.Scanner;
import jpos.events.DataEvent;
import jpos.events.DataListener;

@Component
public class ScannerDeviceWrapper extends AbstractDeviceWrapper<Scanner, ServiceResult> {
    
    public ServiceResult configure(ScannerConfigRequest req) {
        ServiceResult result = doSynchronized((r) -> {
            Scanner scanner = getDevice(req);
            configure(req, scanner);
            r.setResultStatus(Result.SUCCESS);
        }, req, ServiceResult.class);
        return result;
    }

    @Override
    protected Scanner create(DeviceRequest req) throws JposException {
        Scanner scanner = new Scanner();
        scanner.addDataListener(new ScannerDataListener(scanner, req.getDeviceName()));
        return scanner;
    }

    protected void configure(ScannerConfigRequest req, Scanner scanner) throws JposException {
        if (scanner.getState() == JposConst.JPOS_S_CLOSED) {
            scanner.open(DevicesUtils.getLogicalName(req));
        }

        if (!scanner.getClaimed()) {
            scanner.claim(1000);
        }

        if (!scanner.getDeviceEnabled()) {
            scanner.setDeviceEnabled(true);
        }

        scanner.setDecodeData(req.isDecodeData());
        scanner.setAutoDisable(req.isAutoDisable());
        scanner.setDataEventEnabled(req.isDataEventEnabled());

    }

    class ScannerDataListener implements DataListener {

        String logicalName;
        Scanner scanner;

        public ScannerDataListener(Scanner scanner, String logicalName) {
            this.logicalName = logicalName;
            this.scanner = scanner;
        }

        @Override
        public void dataOccurred(DataEvent e) {
            try {
                byte[] bytes = scanner.getScanData();
                String scanData = bytes != null ? new String(bytes) : null;
                bytes = scanner.getScanDataLabel();
                String scanDataLabel = bytes != null ? new String(bytes) : null;;
                int scanDataType = scanner.getScanDataType();
                boolean autoDisable = scanner.getAutoDisable();
                boolean dataEventEnabled = scanner.getDataEventEnabled();
                boolean deviceEnabled = scanner.getDeviceEnabled();
                boolean freezeEvents = scanner.getFreezeEvents();
                boolean decodeData = scanner.getDecodeData();
                int scanDataCount = scanner.getDataCount();
                scanner.clearInput();
                scanner.clearInputProperties();
                messageService.sendMessage("Devices", logicalName, new ScanMessage(scanData, scanDataLabel, scanDataType, autoDisable,
                        dataEventEnabled, deviceEnabled, freezeEvents, decodeData, scanDataCount));
            } catch (JposException e1) {
                logger.error("", e1);
            }
        }
    }

}
