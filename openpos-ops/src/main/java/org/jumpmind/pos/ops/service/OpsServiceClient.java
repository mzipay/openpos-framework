package org.jumpmind.pos.ops.service;

import java.util.Date;
import java.util.List;

import org.jumpmind.pos.context.model.DeviceModel;
import org.jumpmind.pos.context.service.ContextServiceClient;
import org.jumpmind.pos.ops.model.UnitStatus;
import org.jumpmind.pos.ops.model.UnitStatusModel;
import org.jumpmind.pos.ops.model.UnitType;
import org.jumpmind.pos.trans.model.BusinessDate;
import org.jumpmind.pos.user.model.UserModel;

public class OpsServiceClient {

    OpsService opsService;

    ContextServiceClient contextServiceClient;

    public OpsServiceClient(OpsService opsService, ContextServiceClient contextServiceClient) {
        this.opsService = opsService;
        this.contextServiceClient = contextServiceClient;
    }

    public boolean isStoreOpen() {
        DeviceModel device = contextServiceClient.getDevice();
        GetStatusResult results = opsService.getUnitStatus(UnitType.STORE.name(), device.getBusinessUnitId());
        UnitStatusModel unitStatus = results.getUnitStatus(device.getBusinessUnitId());
        if (unitStatus == null || unitStatus.getUnitStatus().equals(UnitStatus.CLOSED.name())) {
            return false;
        } else {
            return true;
        }
    }
    
    public BusinessDate getCurrentBusinessDate() {
        DeviceModel device = contextServiceClient.getDevice();
        GetStatusResult results = opsService.getUnitStatus(UnitType.STORE.name(), device.getBusinessUnitId());
        UnitStatusModel unitStatus = results.getUnitStatus(device.getBusinessUnitId());
        if (unitStatus != null && unitStatus.getUnitStatus().equals(UnitStatus.OPEN.name())) {
            return BusinessDate.toBusinessDate(unitStatus.getBusinessDate());
        } else {
            return null;
        }
    }

    public boolean isDeviceOpen() {
        DeviceModel device = contextServiceClient.getDevice();
        GetStatusResult results = opsService.getUnitStatus(UnitType.DEVICE.name(), device.getDeviceId());
        UnitStatusModel unitStatus = results.getUnitStatus(device.getDeviceId());
        if (unitStatus == null || unitStatus.getUnitStatus().equals(UnitStatus.CLOSED.name())) {
            return false;
        } else {
            return true;
        }
    }

    public boolean areDevicesOpen() {
        GetStatusResult results = opsService.getUnitStatus(UnitType.DEVICE.name(), "*");
        List<UnitStatusModel> statuses = results.getUnitStatuses();
        for (UnitStatusModel unitStatus : statuses) {
            if (unitStatus.getUnitStatus().equals(UnitStatus.OPEN.name())) {
                return true;
            }
        }
        return false;
    }

    public void openStore(UserModel user, BusinessDate date) {
        StatusChangeRequest request = new StatusChangeRequest();
        request.setBusinessDate(date.toString());
        request.setBusinessUnitId(contextServiceClient.getDevice().getBusinessUnitId());
        request.setNewStatus(UnitStatus.OPEN.name());
        request.setRequestingDeviceId(contextServiceClient.getDevice().getDeviceId());
        request.setTimeOfRequest(new Date());
        request.setUnitId(request.getBusinessUnitId());
        request.setUnitType(UnitType.STORE.name());
        request.setUsername(user.getUsername());
        opsService.updateUnitStatus(request);
    }

    public void closeStore(UserModel user, BusinessDate date) {
        StatusChangeRequest request = new StatusChangeRequest();
        request.setBusinessDate(date.toString());
        request.setBusinessUnitId(contextServiceClient.getDevice().getBusinessUnitId());
        request.setNewStatus(UnitStatus.CLOSED.name());
        request.setRequestingDeviceId(contextServiceClient.getDevice().getDeviceId());
        request.setTimeOfRequest(new Date());
        request.setUnitId(request.getBusinessUnitId());
        request.setUnitType(UnitType.STORE.name());
        request.setUsername(user.getUsername());
        opsService.updateUnitStatus(request);
    }

    public void openDevice(UserModel user, BusinessDate date) {
        StatusChangeRequest request = new StatusChangeRequest();
        request.setBusinessDate(date.toString());
        request.setBusinessUnitId(contextServiceClient.getDevice().getBusinessUnitId());
        request.setNewStatus(UnitStatus.OPEN.name());
        request.setRequestingDeviceId(contextServiceClient.getDevice().getDeviceId());
        request.setTimeOfRequest(new Date());
        request.setUnitId(contextServiceClient.getDevice().getDeviceId());
        request.setUnitType(UnitType.DEVICE.name());
        request.setUsername(user.getUsername());
        opsService.updateUnitStatus(request);
    }

    public void closeDevice(UserModel user, BusinessDate date) {
        StatusChangeRequest request = new StatusChangeRequest();
        request.setBusinessDate(date.toString());
        request.setBusinessUnitId(contextServiceClient.getDevice().getBusinessUnitId());
        request.setNewStatus(UnitStatus.CLOSED.name());
        request.setRequestingDeviceId(contextServiceClient.getDevice().getDeviceId());
        request.setTimeOfRequest(new Date());
        request.setUnitId(contextServiceClient.getDevice().getDeviceId());
        request.setUnitType(UnitType.DEVICE.name());
        request.setUsername(user.getUsername());
        opsService.updateUnitStatus(request);
    }

}
