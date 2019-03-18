package org.jumpmind.pos.core.screeninterceptor;

import java.util.Map;

import org.jumpmind.pos.core.content.IContentProvider;
import org.jumpmind.pos.core.ui.UIMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class ContentScreenPropertyStrategy implements IScreenPropertyStrategy {

    @Value("${openpos.ui.content.provider:default}")
    String provider;

    @Autowired
    protected Map<String, IContentProvider> contentProviders;

    @Override
    public Object doStrategy(String appId, String deviceId, Object property, Class<?> clazz, UIMessage screen,
            Map<String, Object> screenContext) {

        IContentProvider contentProvider = getContentProvider();

        if (String.class.equals(clazz) && contentProvider != null) {
            String value = (String) property;
            if (value != null && value.startsWith("content:")) {
                String[] parts = value.split(":");
                String key = null;
                if (parts.length == 2) {
                    key = parts[1];
                }

                if (key != null) {
                    return contentProvider.getContentUrl(deviceId, key);
                }
            }
        }

        return property;
    }

    private IContentProvider getContentProvider() {
        IContentProvider contentProvider = null;

        if (provider != null) {
            if (contentProviders.containsKey(provider)) {
                contentProvider = contentProviders.get(provider);
            }
        }

        return contentProvider;
    }

}
