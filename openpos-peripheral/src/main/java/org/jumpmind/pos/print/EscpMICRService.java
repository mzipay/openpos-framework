package org.jumpmind.pos.print;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jpos.JposConst;
import jpos.JposException;
import jpos.events.DataEvent;
import jpos.services.EventCallbacks;
import jpos.services.MICRService19;

public class EscpMICRService implements MICRService19 {

	
    private IOpenposPrinter printer;
    private String printerHostname = "";
    private String printerPort = "";

    public final static int DEVICE_VERSION = 1009000;

    public final static String STATUS_OPEN = "0";
    public final static String STATUS_CLOSED = "1";

    protected final Log logger = LogFactory.getLog(getClass());

    protected EventCallbacks callbacks;

    private int state = JposConst.JPOS_S_CLOSED;
    private boolean open;
    private boolean claimed;
    private boolean enabled;
    private boolean freezeEvents;
    private int powerNotify;
    
    private String rawData;
    
    private String rawDataFormat = "TOAD";
    
    public void setPrinter(IOpenposPrinter printer) {
        this.printer = printer;
    }

    public IOpenposPrinter getPrinter() {
        return this.printer;
    }

    public void setPrinterHostname(String printerHostname) {
        this.printerHostname = printerHostname;
    }

    public String getPrinterHostname() {
        return this.printerHostname;
    }

    public void setPrinterPort(String printerPort) {
        this.printerPort = printerPort;
    }

    public String getPrinterPort() {
        return this.printerPort;
    }

    public void init() throws JposException {
        if (printer == null) {
            printer = new EscpPOSPrinter();
            Map<String, Object> settings = new HashMap<>();
            settings.put("hostName", printerHostname);
            settings.put("port", printerPort);
            settings.put("connectionClass", "org.jumpmind.pos.print.SocketConnectionFactory");
            settings.put("printerCommandLocations", "esc_p.properties,epson.properties");
            this.printer.init(settings, null);
        }
    }

    protected void openPrinter() throws JposException {
        this.printer.open("micrprinter", null);
    }

    protected void closePrinter() throws JposException {
        this.printer.close();
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

	@Override
	public boolean getCapValidationDevice() throws JposException {
		return false;
	}

	@Override
	public boolean getAutoDisable() throws JposException {
		return false;
	}

	@Override
	public void setAutoDisable(boolean autoDisable) throws JposException {
		
	}

	@Override
	public String getAccountNumber() throws JposException {
		return null;
	}

	@Override
	public String getAmount() throws JposException {
		return null;
	}
 
	@Override
	public String getBankNumber() throws JposException {
		return null;
	}

	@Override
	public int getCheckType() throws JposException {
		return 0;
	}

	@Override
	public int getCountryCode() throws JposException {
		return 0;
	}

	@Override
	public int getDataCount() throws JposException {
		return 0;
	}

	@Override
	public boolean getDataEventEnabled() throws JposException {
		return true;
	}

	@Override
	public void setDataEventEnabled(boolean dataEventEnabled) throws JposException {
		
	}

	@Override
	public String getEPC() throws JposException {
		return null;
	}

	@Override
	public String getRawData() throws JposException {
		return this.rawData;
	}

	@Override
	public String getSerialNumber() throws JposException {
		return null;
	}

	@Override
	public String getTransitNumber() throws JposException {
		return null;
	}

	@Override
	public void beginInsertion(int timeout) throws JposException {
		this.printer.beginInsertion(timeout);
	}

	@Override
	public void beginRemoval(int timeout) throws JposException {
		
	}

	@Override
	public void clearInput() throws JposException {
		this.rawData = null;
	}

	@Override
	public void endInsertion() throws JposException {
		this.rawData = this.printer.readMicr();
		if(this.rawData !=null && "TOAD".equalsIgnoreCase(this.rawDataFormat) ) {
			getTOADFormat();
		}
		this.callbacks.fireDataEvent(new DataEvent(this.rawData, 1));
	}

	private void getTOADFormat() {
		this.rawData = this.rawData.replace(')', 'T').replace('(', 'O').replace('\'', 'D').replace('&', 'A');
	}

	@Override
	public void endRemoval() throws JposException {
		
	}

	@Override
	public String getCheckHealthText() throws JposException {
		return "";
	}

	@Override
	public boolean getClaimed() throws JposException {
		return this.claimed;
	}

	@Override
	public boolean getDeviceEnabled() throws JposException {
		return this.enabled;
	}

	@Override
	public void setDeviceEnabled(boolean deviceEnabled) throws JposException {

		this.enabled = deviceEnabled;
	}

	@Override
	public String getDeviceServiceDescription() throws JposException {
		return "MICR";
	}

	@Override
    public int getDeviceServiceVersion() throws JposException {
        return DEVICE_VERSION;
    }


    public void setFreezeEvents(boolean freezeEvents) throws JposException {
        checkIfOpen();
        this.freezeEvents = freezeEvents;
    }

    public boolean getFreezeEvents() throws JposException {
        return freezeEvents;
    }

    public int getPowerNotify() throws JposException {
        return powerNotify;
    }

    public void setPowerNotify(int powerNotify) {
        this.powerNotify = powerNotify;
    }
	@Override
	public String getPhysicalDeviceDescription() throws JposException {
		return "ESPMICR service";
	}

	@Override
	public String getPhysicalDeviceName() throws JposException {
		return "ESCP MICR";
	}

	@Override
	public int getState() throws JposException {
		return this.state;
	}

	@Override
	public void claim(int timeout) throws JposException {
        checkIfOpen();
        claimed = true;
        logger.info("The device was claimed");
		
	}

	@Override
	public void close() throws JposException {
        checkIfOpen();
        this.open = false;
        this.state = JposConst.JPOS_S_CLOSED;

        // Also need to reset all the member variables
        callbacks = null;
        enabled = false;
        freezeEvents = false;
        claimed = false;
        this.rawData = null;
        logger.info("The device was closed");
		
	}
    protected void checkIfOpen() throws JposException {
        if (!open)
            throw new JposException(JposConst.JPOS_E_CLOSED, "Service is not open.");
    }

	@Override
	public void checkHealth(int level) throws JposException {
		
	}

	@Override
	public void directIO(int command, int[] data, Object object) throws JposException {
		
	}

	@Override
	public void open(String logicalName, EventCallbacks cb) throws JposException {
		 
        if (open) {
            throw new JposException(JposConst.JPOS_E_ILLEGAL, "Service is already open.");
        }
        this.open = true;
        this.state = JposConst.JPOS_S_IDLE;
        this.callbacks = cb;
        logger.info("The device was opened");
	}
    protected void checkIfClaimed() throws JposException {
        if (!claimed)
            throw new JposException(JposConst.JPOS_E_NOTCLAIMED, "Device is not claimed.");
    }
	@Override
	public void release() throws JposException {

        checkIfOpen();
        checkIfClaimed();
        this.claimed = false;
        this.enabled = false;
        this.state = JposConst.JPOS_S_IDLE;
        logger.info("The device was released");	}

	@Override
    public void deleteInstance() throws JposException {
        checkIfOpen();
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
    public EventCallbacks getCallbacks() {
        return callbacks;
    }

    public void setCallbacks(EventCallbacks callbacks) {
        this.callbacks = callbacks;
    }

	public void setRawDataFormat(String rawDataFormat) {
		this.rawDataFormat = rawDataFormat;
	}

	public String getRawDataFormat() {
		return rawDataFormat;
	}

}
