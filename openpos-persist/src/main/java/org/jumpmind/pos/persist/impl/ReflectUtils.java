package org.jumpmind.pos.persist.impl;

import java.lang.reflect.Field;

import org.apache.commons.lang.reflect.FieldUtils;
import org.jumpmind.pos.persist.PersistException;

public class ReflectUtils {

    public static void setProperty(Object target, String propertyName, Object value) {
        value = messageNulls(target, propertyName, value);
        try {            
            Class<? extends Object> clazz = target.getClass();
            while (clazz != Object.class) {
                Field field = null;
                try {
                    field = clazz.getDeclaredField(propertyName);                    
                } catch (NoSuchFieldException ex2) {
                }
                if (field != null) {
                    field.setAccessible(true);
                    field.set(target, value);
                    return;
                }
                clazz = clazz.getSuperclass();
            }
        } catch (Exception ex) {
            throw new PersistException(String.format("Failed to set field '%s' on target '%s' to value '%s'", propertyName, target, value), ex);
        }
        
        throw new PersistException(String.format("Could not locate field '%s' on target '%s' to set to value '%s'", propertyName, target, value));
    }
    
    private static Object messageNulls(Object target, String propertyName, Object value) {
        if (value != null) {
            return value;
        }
        Field field = FieldUtils.getDeclaredField(target.getClass(), propertyName, true);
        if (field.getType().isAssignableFrom(boolean.class)) {
            return false;
        }
        
        return value;
    }    
    
}
