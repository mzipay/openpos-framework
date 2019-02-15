package org.jumpmind.pos.core.screeninterceptor;

import org.jumpmind.pos.core.content.IContentProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(110)
@Scope("prototype")
public class ContentScreenPropertyStrategy implements IScreenPropertyStrategy {

    @Autowired(required = false)
    IContentProvider contentProvider;

    @Override
    public Object doStrategy(String appId, String deviceId, Object property, Class<?> clazz) {
        if (String.class.equals(clazz) && contentProvider != null) {
            String value = (String) property;
            if (value != null && value.startsWith("content:")) {
                String[] parts = value.split(":");
                String group = "common";
                String key = null;
                if (parts.length >= 3) {
                    group = parts[1];
                    key = parts[2];
                } else if (parts.length == 2) {
                    key = parts[1];
                }

                if (key != null) {
                    return contentProvider.getContentUrl(group, key);
                }
            }
        }

        return property;
    }

}
