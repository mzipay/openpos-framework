package org.jumpmind.pos.core.service;

import java.util.HashMap;
import java.util.function.Consumer;

import org.jumpmind.pos.core.device.IDeviceRequest;
import org.jumpmind.pos.core.device.IDeviceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DeviceService implements IDeviceService {
    @Autowired
    private SimpMessagingTemplate template;

    HashMap<String, Consumer<IDeviceResponse>> requestToResponseMap;
    
    
    @Override
    public void send(String appId, String nodeId, IDeviceRequest request,  Consumer<IDeviceResponse> responseHandler) {
        
        // TODO: Put a timestamp on the key or value so that old requests can be periodically purged.
        this.requestToResponseMap.put(String.format("%s-%s", request.getDeviceId(), request.getRequestId()), responseHandler);
        
        // TODO: May need to add a client id that is registered upon initial connection to the server so that the
        // message is only handled by one client
        this.template.convertAndSend(String.format("/topic/app/%s/node/%s", appId, nodeId), request);
    }
    
    // Need a receive method to receive the response
    @RequestMapping(method = RequestMethod.GET, value = "app/{appId}/node/{nodeId}/device/{deviceId}/{payload}")
    public void onDeviceResponse() {
        // Get the request Id from the DeviceResponse payload to look up the request and then invoke the callback.
    }
    
}
