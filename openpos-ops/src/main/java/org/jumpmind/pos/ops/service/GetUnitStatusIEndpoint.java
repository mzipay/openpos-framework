package org.jumpmind.pos.ops.service;

import org.jumpmind.pos.service.Endpoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(transactionManager = "opsTxManager")
public class GetUnitStatusIEndpoint {

    @Endpoint("/unitStatus/{unitType}/{unitId}")
    public GetStatusResult getUnitStatus(String unitType, String unitId) {
        return new GetStatusResult();
    }
}
