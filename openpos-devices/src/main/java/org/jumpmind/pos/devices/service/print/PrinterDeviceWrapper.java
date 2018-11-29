package org.jumpmind.pos.devices.service.print;

import java.util.List;

import org.jumpmind.pos.devices.service.AbstractDeviceWrapper;
import org.jumpmind.pos.devices.service.DeviceRequest;
import org.jumpmind.pos.service.ServiceResult;
import org.jumpmind.pos.service.ServiceResult.Result;
import org.springframework.stereotype.Component;

import jpos.JposConst;
import jpos.JposException;
import jpos.POSPrinter;

@Component
public class PrinterDeviceWrapper extends AbstractDeviceWrapper<POSPrinter, ServiceResult> {

    public ServiceResult print(PrintRequest req) {
        ServiceResult result = doSynchronized((r) -> {
            POSPrinter printer = getDevice(req);
            print(printer, req);
            r.setResultStatus(Result.SUCCESS);
        }, req, ServiceResult.class);
        return result;
    }

    protected void print(POSPrinter printer, PrintRequest req) throws JposException {
        try {
            if (printer.getState() == JposConst.JPOS_S_CLOSED) {
                printer.open(req.getDeviceName());
            }

            if (!printer.getClaimed()) {
                printer.claim(10000);
            }

            if (!printer.getDeviceEnabled()) {
                printer.setDeviceEnabled(true);
            }

            if (printer.getAsyncMode()) {
                printer.setAsyncMode(false);
            }

            PrintableDocument document = req.getDocument();
            int station = document.getStation();

            List<DocumentElement> elements = document.getElements();
            for (DocumentElement documentElement : elements) {
                if (documentElement instanceof Line) {
                    Line item = (Line) documentElement;
                    printer.printNormal(station, item.getData());
                } else if (documentElement instanceof Barcode) {
                    Barcode item = (Barcode) documentElement;
                    printer.printBarCode(station, item.getData(), item.getSymbology(), item.getHeight(), item.getWidth(), item.getAlignment(), item.getTextPosition());
                } else if (documentElement instanceof FileImage) {
                    FileImage item = (FileImage) documentElement;
                    printer.printBitmap(station, item.getFileName(), item.getWidth(), item.getAlignment());
                } else if (documentElement instanceof MemoryImage) {
                    MemoryImage item = (MemoryImage) documentElement;
                    printer.printMemoryBitmap(station, item.getData(), item.getType(), item.getWidth(), item.getAlignment());
                } else if (documentElement instanceof RuleLine) {
                    RuleLine item = (RuleLine) documentElement;
                    printer.drawRuledLine(station, item.getPositionList(), item.getLineDirection(), item.getLineWidth(), item.getLineStyle(), item.getLineColor());
                } else if (documentElement instanceof CutPaper) {
                    CutPaper item = (CutPaper) documentElement;
                    printer.cutPaper(item.getPercentage());
                }
            }

        } catch (JposException ex) {
            try {
                printer.setDeviceEnabled(false);
                printer.release();
                printer.close();
            } catch (Exception ex1) {
            }
            throw ex;
        }

    }

    @Override
    protected POSPrinter create(DeviceRequest req) throws JposException {
        POSPrinter printer = new POSPrinter();
        return printer;
    }

}
