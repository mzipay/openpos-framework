package org.jumpmind.pos.print;

import lombok.Data;

import javax.usb.UsbDevice;
import java.util.ArrayList;
import java.util.List;

@Data
public class FindUsbDeviceResult {

    private UsbDevice matchingUsbDevice;
    private List<UsbDevice> foundUsbDevices = new ArrayList<UsbDevice>();
}
