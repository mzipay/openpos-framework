package org.jumpmind.pos.devices.javapos;

import jpos.BaseControl;
import jpos.events.DataEvent;
import jpos.events.DirectIOEvent;
import jpos.events.ErrorEvent;
import jpos.events.OutputCompleteEvent;
import jpos.events.StatusUpdateEvent;
import jpos.services.EventCallbacks;

public class EventCallbacksAdapter implements EventCallbacks {

    DataEvent dataEvent;
    
    @Override
    public void fireDataEvent(DataEvent e) {
        this.dataEvent = e;
    }

    @Override
    public void fireDirectIOEvent(DirectIOEvent e) {
    }

    @Override
    public void fireErrorEvent(ErrorEvent e) {
    }

    @Override
    public void fireOutputCompleteEvent(OutputCompleteEvent e) {
    }

    @Override
    public void fireStatusUpdateEvent(StatusUpdateEvent e) {
    }

    @Override
    public BaseControl getEventSource() {
        return null;
    }
    
    public DataEvent waitForDataEvent(int timeoutInMs) {
        long ts = System.currentTimeMillis();
        while (dataEvent == null && System.currentTimeMillis() - ts < timeoutInMs) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        return dataEvent;
    }
    
    

}
