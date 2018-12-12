package org.jumpmind.pos.devices.client;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.lang.reflect.Type;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jumpmind.pos.util.model.Message;
import org.jumpmind.pos.util.web.ConfiguredRestTemplate;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import jpos.JposConst;
import jpos.JposException;
import jpos.loader.JposServiceInstance;
import jpos.services.BaseService;
import jpos.services.EventCallbacks;

abstract public class AbstractBaseService implements BaseService, JposServiceInstance {

    public final static int DEVICE_VERSION = 1009000;

    protected final Log logger = LogFactory.getLog(getClass());

    protected EventCallbacks callbacks;

    protected int state = JposConst.JPOS_S_CLOSED;
    protected boolean open;
    protected boolean claimed;
    protected boolean enabled;
    protected boolean freezeEvents;
    protected int powerNotify;
    protected String deviceName;
    protected String logicalDeviceName;
    protected int port = -1;
    protected boolean sslEnabled = false;
    protected String profile = "default";
    protected String serverName = "localhost";
    private RestTemplate restTemplate;
    private WebSocketStompClient stompClient;
    private boolean subscribed = false;

    public abstract void reset();
    
    public int getDeviceServiceVersion() throws JposException {
        return DEVICE_VERSION;
    }

    protected void registerStompClient(String deviceName, Type typeExpected, IMessageListener<Message> listener) {
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.setTaskScheduler(new ConcurrentTaskScheduler());

        StompSessionHandler sessionHandler = new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                StringBuilder topic = new StringBuilder(128);
                topic.append("/topic/app/").append("Devices").append("/node/").append(deviceName);
                session.subscribe(topic.toString(), this);
                subscribed = true;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                listener.handle((Message)payload);
            }
            
            @Override
            public Type getPayloadType(StompHeaders headers) {                
                return typeExpected;
            }            

        };
        stompClient.connect(getWsUrl(), sessionHandler);

        long ts = System.currentTimeMillis();
        while (!subscribed && System.currentTimeMillis()-ts < 5000) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }

    protected String getWsUrl() {
        return (sslEnabled ? "wss://" : "ws://") + serverName + (port > 0 ? ":" + port : "") + "/api/websocket";

    }

    protected String getBaseHttpUrl() {
        return (sslEnabled ? "https://" : "http://") + serverName + (port > 0 ? ":" + port : "") + "/devices";
    }

    protected RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            restTemplate = new ConfiguredRestTemplate();
        }
        return restTemplate;
    }

    protected void checkIfOpen() throws JposException {
        if (!open)
            throw new JposException(JposConst.JPOS_E_CLOSED, "Service is not open.");
    }

    protected void checkIfClaimed() throws JposException {
        if (!claimed)
            throw new JposException(JposConst.JPOS_E_NOTCLAIMED, "Device is not claimed.");
    }

    public void deleteInstance() throws JposException {
        checkIfOpen();
    }

    public void open(String deviceName, EventCallbacks eventcallbacks) throws JposException {
        if (open) {
            throw new JposException(JposConst.JPOS_E_ILLEGAL, "Service is already open.");
        }
        this.open = true;
        this.logicalDeviceName = deviceName;
        if (isBlank(this.deviceName)) {
            this.deviceName = deviceName;
        }
        this.state = JposConst.JPOS_S_IDLE;
        this.callbacks = eventcallbacks;
        logger.info("The device was opened");
    }

    public void close() throws JposException {
        checkIfOpen();
        this.open = false;
        this.state = JposConst.JPOS_S_CLOSED;

        // Also need to reset all the member variables
        callbacks = null;
        enabled = false;
        freezeEvents = false;
        claimed = false;
        reset();
        
        if (stompClient != null) {
            stompClient.stop();
        }
        
        logger.info("The device was closed");
    }

    public void claim(int timeout) throws JposException {
        checkIfOpen();
        claimed = true;
        logger.info("The device was claimed");
    }

    public void release() throws JposException {
        checkIfOpen();
        checkIfClaimed();
        this.claimed = false;
        this.enabled = false;
        this.state = JposConst.JPOS_S_IDLE;
        logger.info("The device was released");
    }

    public int getState() throws JposException {
        return this.state;
    }

    public boolean getClaimed() throws JposException {
        return claimed;
    }

    public boolean getDeviceEnabled() throws JposException {
        return enabled;
    }

    public void setDeviceEnabled(boolean enabled) throws JposException {
        checkIfOpen();
        checkIfClaimed();
        this.enabled = enabled;
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

    public EventCallbacks getCallbacks() {
        return callbacks;
    }

    public void setCallbacks(EventCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    public String getCheckHealthText() throws JposException {
        return null;
    }

    @Override
    public String getDeviceServiceDescription() throws JposException {
        return null;
    }

    @Override
    public String getPhysicalDeviceDescription() throws JposException {
        return null;
    }

    @Override
    public String getPhysicalDeviceName() throws JposException {
        return null;
    }

    @Override
    public void checkHealth(int level) throws JposException {
    }

    @Override
    public void directIO(int command, int[] data, Object object) throws JposException {
    }
    
    interface IMessageListener<T extends Message> {
        public void handle(T message);
    }

}
