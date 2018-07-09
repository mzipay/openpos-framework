package org.jumpmind.pos.ops.service;

import org.jumpmind.pos.service.EndpointDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ops")
public class OpsService {

    @Autowired
    private EndpointDispatcher endpointDispatcher;

    @RequestMapping("/unitStatus/{unitType}/{unitId}")
    public GetStatusResult getUnitStatus(
            @RequestParam(value = "unitType") String unitType,
            @RequestParam(value = "unitId", defaultValue = "*") String unitId) {
        return endpointDispatcher.dispatch("/unitStatus/{unitType}/{unitId}", unitType, unitId);
    }

    @RequestMapping("/changeUnitStatus")
    public StatusChangeResult updateUnitStatus(StatusChangeRequest request) {
        return endpointDispatcher.dispatch("/changeUnitStatus", request);
    }

}
