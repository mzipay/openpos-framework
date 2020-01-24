package org.jumpmind.pos.print;

import org.apache.commons.lang3.ThreadUtils;
import org.jumpmind.pos.util.AppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.usb.*;
import java.awt.print.PrinterAbortException;
import java.util.List;

public class UsbHelper {

    static final Logger log = LoggerFactory.getLogger(UsbHelper.class);

    public FindUsbDeviceResult findDevice(short vendorId, short productId) {
        try {
            UsbServices services = UsbHostManager.getUsbServices();
            UsbHub rootHub = services.getRootUsbHub();
            FindUsbDeviceResult findUsbDeviceResult = new FindUsbDeviceResult();
            return findDevice(findUsbDeviceResult, rootHub, vendorId, productId);
        } catch (Exception ex) {
            throw new PrintException("Failed to locate device for vendorId=" + vendorId + " productId=" + productId, ex);
        }
    }

    public UsbConnection openUsbConnection(short vendorId, short productId) {
        FindUsbDeviceResult findUsbDeviceResult = findDevice(vendorId, productId);
        UsbDevice usbDevice = findUsbDeviceResult.getMatchingUsbDevice();
        if (usbDevice == null) {
            throw new PrintException("Could not locate USB device for vendorId: " + vendorId + "  productId: " + productId + " ");
        }

        UsbConfiguration usbConfiguration = usbDevice.getActiveUsbConfiguration();
        UsbInterface usbInterface = usbConfiguration.getUsbInterface((byte) 0);

        final int RETRIES = 10;

        claimInterfaceWithRetries(usbInterface, RETRIES);

        UsbEndpoint usbEndpoint = usbInterface.getUsbEndpoint((byte) 1);

        UsbPipe pipe = usbEndpoint.getUsbPipe();

        try {
            if (!pipe.isOpen()) {
                pipe.open();
            }
        } catch (Exception ex) {
            throw new PrintException("Failed to open pipe to USB device vendorId=" + vendorId + " productId=" + productId, ex);
        }

        UsbConnection usbConnection = new UsbConnection();
        usbConnection.setUsbDevice(usbDevice);
        usbConnection.setUsbInterface(usbInterface);
        usbConnection.setUsbEndpoint(usbEndpoint);
        usbConnection.setUsbPipe(pipe);

        return usbConnection;

    }
    public void claimInterfaceWithRetries(UsbInterface usbInterface, int retries) {
        Exception claimException = null;
        do {
            try {
                claimInterface(usbInterface);
            } catch (Exception ex) {
                log.debug("Failed to claim interface, remaining retries: " + retries, ex);
                claimException = ex;
                AppUtils.sleep(2000);
            }
        } while (!usbInterface.isClaimed() && retries-- > 0);

        if (!usbInterface.isClaimed()) {
            if (claimException != null) {
                if (claimException instanceof PrintException) {
                    throw (PrintException)claimException;
                } else {
                    throw new PrintException("Failed to claim USB interface " + usbInterface, claimException);
                }
            } else {
                throw new PrintException("Failed to claim usb interface, reason unknown. " + usbInterface);
            }
        }


    }

    public void claimInterface(UsbInterface usbInterface) {
        if (!usbInterface.isClaimed()) {
            try {
                usbInterface.claim(new UsbInterfacePolicy() {
                    @Override
                    public boolean forceClaim(UsbInterface usbInterface) {
                        return true;
                    }
                });
            } catch (Exception ex) {
                throw new PrintException("Failed to claim USB device: " + usbInterface, ex);
            }
        }
    }


    public FindUsbDeviceResult findDevice(FindUsbDeviceResult findUsbDeviceResult, UsbHub hub, short vendorId, short productId) {

        final int WILDCARD = -1;

        for (UsbDevice device : (List<UsbDevice>) hub.getAttachedUsbDevices()) {
            UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();

            if (log.isInfoEnabled()) {
                try {
                    log.info("Found USB device while scanning for vendorId=" + vendorId + " productId=" + productId + " " +
                            usbDeviceToString(device));
                } catch (Exception ex) {
                    log.debug("Failed to get details while scanning USB device.", ex);
                }

            }

            if ((desc.idVendor() == vendorId || vendorId == WILDCARD) &&
                    (desc.idProduct() == productId || productId == WILDCARD)) {
                findUsbDeviceResult.setMatchingUsbDevice(device);
                return findUsbDeviceResult;
            }
            else if (device.isUsbHub()) {
                findUsbDeviceResult = findDevice(findUsbDeviceResult, (UsbHub) device, vendorId, productId);
                if (findUsbDeviceResult.getMatchingUsbDevice() != null) {
                    return findUsbDeviceResult;
                }
            } else {
                findUsbDeviceResult.getFoundUsbDevices().add(device);
            }
        }
        return null;
    }

    public String usbDeviceToString(UsbDevice usbDevice) {
        try {
            return usbDevice.getUsbDeviceDescriptor() + " device.getManufacturerString()=" + usbDevice.getManufacturerString() + " device.getProductString()=" +
                    usbDevice.getProductString() + " device.getProductString()=" + usbDevice.getSerialNumberString();
        } catch (Exception ex) {
            throw new PrintException("Failed to get device info on USB device.", ex);
        }
    }

}
