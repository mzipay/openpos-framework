package org.jumpmind.pos.trans.service;

import org.jumpmind.pos.service.EndpointDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trans")
public class TransService {

    @Autowired
    private EndpointDispatcher endpointDispatcher;
    
    @RequestMapping("/deviceStatus/{deviceId}")
    public void openBusinessUnit() {
        
    }
}
