package org.jumpmind.pos.i18n.service;

import org.jumpmind.pos.context.model.DeviceModel;
import org.jumpmind.pos.context.model.TagModel;
import org.jumpmind.pos.context.service.ContextServiceClient;

public class i18nServiceClient {

    ContextServiceClient contextServiceClient;
    
    i18nService i18nService;

    public i18nServiceClient(ContextServiceClient contextServiceClient, i18nService i18nService) {
        this.contextServiceClient = contextServiceClient;
        this.i18nService = i18nService;
    }
    
    
    public String getString(
            String base,
            String key,
            Object... args) {
        DeviceModel device = contextServiceClient.getDevice();
        return i18nService.getString(base, key, device.getLocale(), device.getTagValue(TagModel.BRAND_ID_TAG), args);
    }
    
    
    
}
