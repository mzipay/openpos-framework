package org.jumpmind.pos.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class DiscoveryServiceController {

    @Autowired
    ProcessManagerService processManager;

    @GetMapping("personalize")
    public ImpersonalizationResponse impersonalize() {
        return makeImpersonalizationResponse();
    }
    
    @GetMapping("ping")
    public String ping() {
        log.info("Received a ping request");
        return "{ \"pong\": \"true\" }";
    }
    
    @RequestMapping(value = "discover")
    @GetMapping("url")
    public ClientConnectInfo getConnectionUrl(@RequestParam("deviceId") String deviceId) {
        
        try {
            DeviceProcessInfo pi = processManager.queryOrLaunchDeviceProcess(deviceId);
            if (pi != null) {
                if (pi.getStatus() == DeviceProcessStatus.Running) {
                    return processManager.constructClientConnectInfo(pi);
                } else {
                    log.trace("Nothing to return.  Device Process '{}' status is: {}", pi.getDeviceId(), pi.getStatus());
                    // TODO: Need to handle better
                    return null;
                }
            } else {
                log.trace("No Device Process Info for '{}'",deviceId);
                // TODO: Need to handle better
                return null;
            }
        } catch (Exception ex) {
            // TODO: better error handling
            log.error(String.format("Failed to launch process for '%s'", deviceId), ex);
            return null;
        }
    }
    
    @Lookup
    ImpersonalizationResponse makeImpersonalizationResponse() {
        // Will be supplied by Spring context
        return null;
    }

}
