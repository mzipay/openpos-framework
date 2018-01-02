package org.jumpmind.pos.core.service;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class DeviceService implements IDeviceService {
    Logger logger = LoggerFactory.getLogger(getClass());
    public final static int MAX_REQUEST_RETENTION_MINS = 20;

    private int requestSequenceNumber = 1;
    
    @Autowired
    private SimpMessagingTemplate template;

    private HashMap<String, DeviceResponseMapEntry> requestToResponseMap = new HashMap<>();

    
    private String makeRequestResponseMapKey(String appId, String nodeId, String deviceId, String requestId) {
        return String.format("%s-%s-%s-%s", appId, nodeId, deviceId, requestId);
    }
    
    /**
     * Sends a DeviceRequest on the websocket associated with the given appId and node Id.
     */
    @Override
    public CompletableFuture<IDeviceResponse> send(String appId, String nodeId, IDeviceRequest request) {
        CompletableFuture<IDeviceResponse> futureResponse = new CompletableFuture<>();
        if (StringUtils.isBlank(request.getRequestId())) {
            request.setRequestId((requestSequenceNumber++) + "");
        }
        
        String mapKey = makeRequestResponseMapKey(appId, nodeId, request.getDeviceId(), request.getRequestId());
        this.requestToResponseMap.put(mapKey, new DeviceResponseMapEntry(futureResponse));
        
        // TODO: May need to add a client id that is registered upon initial connection to the server so that the
        // message is only handled by one client
        this.template.convertAndSend(String.format("/topic/app/%s/node/%s", appId, nodeId), request);
        return futureResponse;
    }
    
    @MessageMapping("device/app/{appId}/node/{nodeId}/device/{deviceId}")
    public void onDeviceResponse(@DestinationVariable String appId, @DestinationVariable String nodeId, @DestinationVariable String deviceId, DefaultDeviceResponse response) {
        String sourceRequestId = response.getRequestId();
        
        if (StringUtils.isNotBlank(sourceRequestId)) {
            // Check to ensure there is a pending request.  If found, invoke the callback and remove from the cache
            String mapKey = makeRequestResponseMapKey(appId, nodeId, response.getDeviceId(), sourceRequestId);
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
    
    /**
     * Clean up any requests that were not completed.  Currently runs every 5 mins.
     */
    @Scheduled(fixedRate = 5*60*1000)
    protected void cleanOrphanedRequests() {
        List<Entry<String, DeviceResponseMapEntry>> expiredRequests = this.requestToResponseMap.entrySet().stream().filter(
           e -> e.getValue().getDeviceResponseCallback().isDone() || LocalTime.now().minusMinutes(MAX_REQUEST_RETENTION_MINS).compareTo(e.getValue().getTimeAdded()) >= 0 ).collect(Collectors.toList());

        if (expiredRequests.size() > 0) {
            String expiredRequestKeys = expiredRequests.stream().map(e -> e.getKey()).collect(Collectors.joining(", "));
            logger.debug("Removing {} orphaned device requests with IDs: {}", expiredRequests.size(), expiredRequestKeys);
            expiredRequests.forEach(entry -> {
                this.requestToResponseMap.remove(entry.getKey());
            });
            
            logger.info("Device Request cache cleaned, {} entr{} removed. New cache size: {}", expiredRequests.size(), 
                    expiredRequests.size() > 1 ? "ies" : "y", this.requestToResponseMap.size() );
        }        
    }
    
    static class DeviceResponseMapEntry {
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
