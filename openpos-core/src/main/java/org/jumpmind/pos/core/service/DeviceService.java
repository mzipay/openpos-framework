package org.jumpmind.pos.core.service;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jumpmind.pos.core.device.DefaultDeviceResponse;
import org.jumpmind.pos.core.device.IDeviceRequest;
import org.jumpmind.pos.core.device.IDeviceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class DeviceService implements IDeviceService {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SimpMessagingTemplate template;

    private HashMap<String, DeviceResponseMapEntry> requestToResponseMap = new HashMap<>();
    private ObjectMapper mapper = new ObjectMapper();

    
    @Override
    public CompletableFuture<IDeviceResponse> send(String appId, String nodeId, IDeviceRequest request) {
        CompletableFuture<IDeviceResponse> futureResponse = new CompletableFuture<>();
        String mapKey = String.format("%s-%s", request.getDeviceId(), request.getRequestId());
        this.requestToResponseMap.put(mapKey, new DeviceResponseMapEntry(futureResponse));
        
        // TODO: May need to add a client id that is registered upon initial connection to the server so that the
        // message is only handled by one client
        this.template.convertAndSend(String.format("/topic/app/%s/node/%s", appId, nodeId), request);
        return futureResponse;
    }
    
    // Need a receive method to receive the response
    //@RequestMapping(method = RequestMethod.GET, value = "app/{appId}/node/{nodeId}/device/{deviceId}/{deviceResponse}")
    // TODO: map this to a better URL?
    @MessageMapping("device/app/{appId}/node/{nodeId}/device/{deviceId}/")
    public void onDeviceResponse(@DestinationVariable String appId, @DestinationVariable String nodeId, @DestinationVariable String deviceId, DefaultDeviceResponse response) {
//        IDeviceResponse response = null;
        String sourceRequestId = response.getRequestId();
        
        /*
        try {
            response = mapper.convertValue(deviceResponse, DefaultDeviceResponse.class);
            sourceRequestId = response.getRequestId();
        } catch (Exception ex) {
            logger.error(
                String.format("Failed to convert device response received to DeviceResponse object for device id '%s'.  response sent: %s", 
                    deviceId, deviceResponse), 
                ex 
            );
        }
        */
        if (StringUtils.isNotBlank(sourceRequestId)) {
            // Check to ensure there is a pending request.  If found, invoke the callback and remove from the cache
            DeviceResponseMapEntry responseEntry = this.requestToResponseMap.get(sourceRequestId);
           if (responseEntry != null) {
                this.requestToResponseMap.remove(sourceRequestId);
                logger.trace("Invoking response callback for source requestId '{}'...", sourceRequestId);
                responseEntry.getDeviceResponseCallback().complete(response);
            } else {
                logger.warn("Callback handler not found or no longer exists for request id '{}', response will be dropped.", sourceRequestId);
            }
        } else {
            logger.warn("Response did not have a source/originating requestId associated with it, won't be able to execute callback for device '{}' to return the response", deviceId);
        }
        
    }
    
    
    static class DeviceResponseMapEntry {
//        Consumer<IDeviceResponse> deviceResponseCallback;
        CompletableFuture<IDeviceResponse> futureResponse;
        LocalTime timeAdded;
        
        DeviceResponseMapEntry(CompletableFuture<IDeviceResponse> callback) {
            this.futureResponse = callback;
            this.timeAdded = LocalTime.now();
        }
        
        public CompletableFuture<IDeviceResponse> getDeviceResponseCallback() {
            return futureResponse;
        }
        public void setDeviceResponseCallback(CompletableFuture<IDeviceResponse> deviceResponseCallback) {
            this.futureResponse = deviceResponseCallback;
        }
        public LocalTime getTimeAdded() {
            return timeAdded;
        }
        public void setTimeAdded(LocalTime timeAdded) {
            this.timeAdded = timeAdded;
        }
        
    }
}
