package org.jumpmind.pos.persist.impl;

import java.lang.reflect.Field;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.jumpmind.pos.persist.PersistException;

public class ReflectUtils {

    public static void setProperty(Object target, String propertyName, Object value) {
        value = messageNulls(target, propertyName, value);
        try {
            Field field = getAccessibleField(target, propertyName);
            try {                        
                field.set(target, value);
                return;
            } catch (Exception ex) {
                try {                            
                    BeanUtils.copyProperty(target, propertyName, value);
                    return;
                } catch (Exception ex2) {
                    throw ex;
                }
            }
        } catch (Exception ex) {
            throw new PersistException(String.format("Failed to set field '%s' on target '%s' to value '%s'", propertyName, target, value), ex);
        }
    }

    private static Object messageNulls(Object target, String propertyName, Object value) {
        if (value != null) {
            return value;
        }
        Field field = getAccessibleField(target, propertyName);
        if (field != null && field.getType().isAssignableFrom(boolean.class)) {
            return false;
        }
        
        return value;
    }
    
    private static Field getAccessibleField(Object target, String propertyName) {
        Class<? extends Object> clazz = target.getClass();
        while (clazz != Object.class) {
            Field field = null;
            try {
                field = clazz.getDeclaredField(propertyName);                    
            } catch (NoSuchFieldException ex2) {
            }
            if (field != null) {
                field.setAccessible(true);
                return field;
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }
    
}
