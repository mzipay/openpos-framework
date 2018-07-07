package org.jumpmind.pos.ops.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jumpmind.pos.context.model.DeviceModel;
import org.jumpmind.pos.context.service.ContextServiceClient;
import org.jumpmind.pos.ops.model.UnitStatus;
import org.jumpmind.pos.ops.model.UnitStatusConstants;

public class OpsServiceClient {

    OpsService opsService;
    
    ContextServiceClient contextServiceClient;

    public OpsServiceClient(OpsService opsService, ContextServiceClient contextServiceClient) {
        this.opsService = opsService;
        this.contextServiceClient = contextServiceClient;
    }
    
    public boolean isStoreOpen() {
        DeviceModel device = contextServiceClient.getDevice();
        GetStatusResult results = opsService.getUnitStatus(UnitStatusConstants.UNIT_TYPE_STORE, device.getBusinessUnitId());
        UnitStatus unitStatus = results.getUnitStatus(device.getBusinessUnitId());
        if (unitStatus == null || unitStatus.getUnitStatus().equals(UnitStatusConstants.STATUS_CLOSED)) {
            return false;
        } else {
            return true;
        }
    }
    
    public void openStore() {
        StatusChangeRequest request = new StatusChangeRequest();
        // TODO where to get the business date from?
        request.setBusinessDay(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        request.setBusinessUnitId(contextServiceClient.getDevice().getBusinessUnitId());
        request.setNewStatus(UnitStatusConstants.STATUS_OPEN);
        request.setRequestingDeviceId(contextServiceClient.getDevice().getDeviceId());
        request.setTimeOfRequest(new Date());
        request.setUnitId(request.getBusinessUnitId());
        request.setUnitType(UnitStatusConstants.UNIT_TYPE_STORE);
        opsService.updateUnitStatus(request);
    }
    
    public void closeStore() {
        StatusChangeRequest request = new StatusChangeRequest();
        // TODO where to get the business date from?
        request.setBusinessDay(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        request.setBusinessUnitId(contextServiceClient.getDevice().getBusinessUnitId());
        request.setNewStatus(UnitStatusConstants.STATUS_CLOSED);
        request.setRequestingDeviceId(contextServiceClient.getDevice().getDeviceId());
        request.setTimeOfRequest(new Date());
        request.setUnitId(request.getBusinessUnitId());
        request.setUnitType(UnitStatusConstants.UNIT_TYPE_STORE);
        opsService.updateUnitStatus(request);
    }

    
    
    
}
