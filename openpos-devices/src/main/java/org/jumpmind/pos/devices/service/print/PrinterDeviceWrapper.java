package org.jumpmind.pos.devices.service.print;

import java.util.List;

import org.jumpmind.pos.devices.DevicesUtils;
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

    public PrinterSettingsResult settings(PrinterSettingsRequest req) {
        ServiceResult result = doSynchronized((r) -> {
            POSPrinter printer = getDevice(req);
            settings(printer, req, (PrinterSettingsResult) r);
            r.setResultStatus(Result.SUCCESS);
        }, req, PrinterSettingsResult.class);
        return (PrinterSettingsResult) result;
    }

    protected void settings(POSPrinter printer, PrinterSettingsRequest req, PrinterSettingsResult res) throws JposException {
        enable(printer, req);

        POSPrinterSettings toSet = req.getSettings();
        if (toSet != null) {
            printer.setAsyncMode(toSet.isAsyncMode());
            printer.setCharacterSet(toSet.getCharacterSet());
            printer.setSlpLetterQuality(toSet.isSlpLetterQuality());
            printer.setRotateSpecial(toSet.getRotateSpecial());
            printer.setSlpLineChars(toSet.getSlpLineChars());
            printer.setSlpLineHeight(toSet.getSlpLineHeight());
            printer.setSlpLineSpacing(toSet.getSlpLineSpacing());
            printer.setFlagWhenIdle(toSet.isFlagWhenIdle());
            printer.setJrnLetterQuality(toSet.isJrnLetterQuality());
            printer.setJrnLineChars(toSet.getJrnLineChars());
            printer.setJrnLineHeight(toSet.getJrnLineHeight());
            printer.setJrnLineSpacing(toSet.getJrnLineSpacing());
            printer.setMapMode(toSet.getMapMode());
            printer.setRecLetterQuality(toSet.isRecLetterQuality());
            printer.setRecLineChars(toSet.getRecLineChars());
            printer.setRecLineHeight(toSet.getRecLineHeight());
            printer.setRecLineSpacing(toSet.getRecLineSpacing());
        }

        POSPrinterSettings settings = new POSPrinterSettings();
        settings.setAsyncMode(printer.getAsyncMode());
        settings.setCharacterSet(printer.getCharacterSet());
        settings.setSlpLetterQuality(printer.getSlpLetterQuality());
        settings.setRotateSpecial(printer.getRotateSpecial());
        settings.setSlpLineChars(printer.getSlpLineChars());
        settings.setSlpLineHeight(printer.getSlpLineHeight());
        settings.setSlpLineSpacing(printer.getSlpLineSpacing());
        settings.setFlagWhenIdle(printer.getFlagWhenIdle());
        settings.setJrnLetterQuality(printer.getJrnLetterQuality());
        settings.setJrnLineChars(printer.getJrnLineChars());
        settings.setJrnLineHeight(printer.getJrnLineHeight());
        settings.setJrnLineSpacing(printer.getJrnLineSpacing());
        settings.setMapMode(printer.getMapMode());
        settings.setRecLetterQuality(printer.getRecLetterQuality());
        settings.setRecLineChars(printer.getRecLineChars());
        settings.setRecLineHeight(printer.getRecLineHeight());
        settings.setRecLineSpacing(printer.getRecLineSpacing());
        settings.setCoverOpen(printer.getCoverOpen());
        res.setSettings(settings);

    }

    protected void enable(POSPrinter printer, DeviceRequest req) throws JposException {
        if (printer.getState() == JposConst.JPOS_S_CLOSED) {
            printer.open(DevicesUtils.getLogicalName(req));
        }

        if (!printer.getClaimed()) {
            printer.claim(10000);
        }

        if (!printer.getDeviceEnabled()) {
            printer.setDeviceEnabled(true);
        }
    }

    protected void print(POSPrinter printer, PrintRequest req) throws JposException {
        try {
            enable(printer, req);

            PrintableDocument document = req.getDocument();
            int station = document.getStation();

            List<DocumentElement> elements = document.getElements();
            for (DocumentElement documentElement : elements) {
                if (documentElement instanceof Line) {
                    Line item = (Line) documentElement;
                    printer.printNormal(station, item.getData());
                } else if (documentElement instanceof Barcode) {
                    Barcode item = (Barcode) documentElement;
                    printer.printBarCode(station, item.getData(), item.getSymbology(), item.getHeight(), item.getWidth(), item.getAlignment(),
                            item.getTextPosition());
                } else if (documentElement instanceof FileImage) {
                    FileImage item = (FileImage) documentElement;
                    printer.printBitmap(station, item.getFileName(), item.getWidth(), item.getAlignment());
                } else if (documentElement instanceof MemoryImage) {
                    MemoryImage item = (MemoryImage) documentElement;
                    printer.printMemoryBitmap(station, item.getData(), item.getType(), item.getWidth(), item.getAlignment());
                } else if (documentElement instanceof RuleLine) {
                    RuleLine item = (RuleLine) documentElement;
                    printer.drawRuledLine(station, item.getPositionList(), item.getLineDirection(), item.getLineWidth(), item.getLineStyle(),
                            item.getLineColor());
                } else if (documentElement instanceof CutPaper) {
                    CutPaper item = (CutPaper) documentElement;
                    printer.cutPaper(item.getPercentage());
                } else if (documentElement instanceof BeginInsert) {
                    BeginInsert item = (BeginInsert) documentElement;
                    printer.beginInsertion(item.getTimeout());
                } else if (documentElement instanceof EndInsert) {
                    printer.endInsertion();
                } else if (documentElement instanceof BeginRemoval) {
                    BeginRemoval item = (BeginRemoval) documentElement;
                    printer.beginRemoval(item.getTimeout());
                } else if (documentElement instanceof EndRemoval) {
                    printer.endRemoval();
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
