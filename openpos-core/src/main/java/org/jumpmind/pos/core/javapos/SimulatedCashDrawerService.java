package org.jumpmind.pos.core.javapos;

import jpos.JposException;
import jpos.services.CashDrawerService19;

public class SimulatedCashDrawerService extends AbstractSimulatedService implements CashDrawerService19 {

    private boolean cashDrawerOpened;

    @Override
    public int getDeviceServiceVersion() throws JposException {
        return 1007000;
    }

    @Override
    public void reset() {
        this.cashDrawerOpened = false;
    }

    public void toggleDrawer(boolean open) {
        if (open) {
            this.cashDrawerOpened = true;
        } else {
            this.cashDrawerOpened = false;
        }
    }

    public boolean getDrawerOpened() throws JposException {
        return this.cashDrawerOpened;
    }

    public void openDrawer() throws JposException {
        toggleDrawer(true);
    }

    public void waitForDrawerClose(int arg0, int arg1, int arg2, int arg3) throws JposException {

    }

    public void compareFirmwareVersion(String arg0, int[] arg1) throws JposException {
    }

    public boolean getCapCompareFirmwareVersion() throws JposException {
        return false;
    }

    public boolean getCapUpdateFirmware() throws JposException {
        return false;
    }

    public void updateFirmware(String arg0) throws JposException {
    }

    public boolean getCapStatisticsReporting() throws JposException {
        return false;
    }

    public boolean getCapUpdateStatistics() throws JposException {
        return false;
    }

    public void resetStatistics(String arg0) throws JposException {
    }

    public void retrieveStatistics(String[] arg0) throws JposException {
    }

    public void updateStatistics(String arg0) throws JposException {
    }

    public boolean getCapStatusMultiDrawerDetect() throws JposException {
        return false;
    }

    public int getCapPowerReporting() throws JposException {
        return 0;
    }

    public int getPowerState() throws JposException {
        return 0;
    }

    public boolean getCapStatus() throws JposException {
        return false;
    }

    public void checkHealth(int arg0) throws JposException {
    }

    public void directIO(int arg0, int[] arg1, Object arg2) throws JposException {
    }

    public String getCheckHealthText() throws JposException {
        return null;
    }

    public String getDeviceServiceDescription() throws JposException {
        return null;
    }

    public String getPhysicalDeviceDescription() throws JposException {
        return null;
    }

    public String getPhysicalDeviceName() throws JposException {
        return null;
    }

    public boolean getCapServiceAllowManagement() throws JposException {
        return false;
    }

}
