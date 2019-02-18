package org.jumpmind.pos.core.screeninterceptor;

import org.jumpmind.pos.core.content.IContentProvider;
import org.springframework.beans.factory.annotation.Autowired;

public class ContentScreenPropertyStrategy implements IScreenPropertyStrategy {

    @Autowired(required = false)
    IContentProvider contentProvider;

    @Override
    public Object doStrategy(String appId, String deviceId, Object property, Class<?> clazz) {
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

}
