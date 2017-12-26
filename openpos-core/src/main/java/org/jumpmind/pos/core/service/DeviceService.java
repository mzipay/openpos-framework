package org.jumpmind.pos.core.service;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

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

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class DeviceService implements IDeviceService {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SimpMessagingTemplate template;

    private HashMap<String, DeviceResponseMapEntry> requestToResponseMap = new HashMap<>();
    private ObjectMapper mapper = new ObjectMapper();

    
    private String makeRequestResponseMapKey(String deviceId, String requestId) {
        return String.format("%s-%s", deviceId, requestId);
    }
    
    @Override
    public CompletableFuture<IDeviceResponse> send(String appId, String nodeId, IDeviceRequest request) {
        CompletableFuture<IDeviceResponse> futureResponse = new CompletableFuture<>();
        String mapKey = makeRequestResponseMapKey(request.getDeviceId(), request.getRequestId());
        this.requestToResponseMap.put(mapKey, new DeviceResponseMapEntry(futureResponse));
        
        // TODO: May need to add a client id that is registered upon initial connection to the server so that the
        // message is only handled by one client
        this.template.convertAndSend(String.format("/topic/app/%s/node/%s", appId, nodeId), request);
        return futureResponse;
    }
    
    // Need a receive method to receive the response
    //@RequestMapping(method = RequestMethod.GET, value = "app/{appId}/node/{nodeId}/device/{deviceId}/{deviceResponse}")
    // TODO: map this to a better URL?
    @MessageMapping("device/app/{appId}/node/{nodeId}/device/{deviceId}")
    public void onDeviceResponse(@DestinationVariable String appId, @DestinationVariable String nodeId, @DestinationVariable String deviceId, DefaultDeviceResponse response) {
        String sourceRequestId = response.getRequestId();
        
        if (StringUtils.isNotBlank(sourceRequestId)) {
            // Check to ensure there is a pending request.  If found, invoke the callback and remove from the cache
            String mapKey = makeRequestResponseMapKey(response.getDeviceId(), sourceRequestId);
            DeviceResponseMapEntry responseEntry = this.requestToResponseMap.get(mapKey);
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
