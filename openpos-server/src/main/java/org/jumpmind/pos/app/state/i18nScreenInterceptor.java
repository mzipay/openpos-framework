package org.jumpmind.pos.app.state;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.jumpmind.pos.context.service.ContextService;
import org.jumpmind.pos.context.service.ContextServiceClient;
import org.jumpmind.pos.core.flow.IScreenInterceptor;
import org.jumpmind.pos.core.screen.Screen;
import org.jumpmind.pos.i18n.service.i18nService;
import org.jumpmind.pos.i18n.service.i18nServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(100)
public class i18nScreenInterceptor implements IScreenInterceptor {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    i18nService i18nService;

    @Autowired
    ContextService contextService;

    i18nServiceClient i18nServiceClient;

    private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();

    public static boolean isWrapperType(Class<?> clazz) {
        return WRAPPER_TYPES.contains(clazz);
    }

    private static Set<Class<?>> getWrapperTypes() {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        return ret;
    }

    @Override
    public Screen intercept(String appId, String deviceId, Screen screen) {
        init(appId, deviceId);
        if (screen != null) {
            Class<?> clazz = screen.getClass();
            processFields(clazz, screen);

        }
        return screen;
    }

    private final void init(String appId, String deviceId) {
        if (i18nServiceClient == null) {
            i18nServiceClient = new i18nServiceClient(new ContextServiceClient(contextService, deviceId), i18nService);
        }
    }

    private final void processFields(Class<?> clazz, Object obj) {
        while (clazz != null && obj != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                Class<?> type = field.getType();
                if (type.equals(String.class)) {
                    replace(field, obj);
                } else if (!isWrapperType(type) && !type.isPrimitive()) {
                    try {
                        field.setAccessible(true);
                        Object fieldObj = field.get(obj);
                        if (fieldObj != null) {
                            processFields(fieldObj.getClass(), fieldObj);
                        }
                    } catch (Exception e) {
                        logger.warn("", e);
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
    }
    
    private final void replace(Field field, Object obj) {
        try {
            field.setAccessible(true);
            String value = (String)field.get(obj);
            if (value != null && value.startsWith("key:")) {
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
                    value = i18nServiceClient.getString(group, key);
                    field.set(obj, value);
                }
                
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            logger.error("Failed to replace key value with i18n resource", e);
        } 
        
    }

}
