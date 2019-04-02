package org.jumpmind.pos.util.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class ITypeCodeRegistry {

    private static Map<Class<? extends ITypeCode>,Set<? extends ITypeCode>> registry = new HashMap<>();
    private static final Object registryLock = new Object();


    public static Set<Class<? extends ITypeCode>> types() {
        return Collections.unmodifiableSet(registry.keySet());
    }
    
    @SuppressWarnings("unchecked")
    public static <T extends ITypeCode> Set<T> values(Class<T> clazz) {
        return Collections.unmodifiableSet((Set<T>)registry.get(clazz));
    }

    public static boolean exists(Class<? extends ITypeCode> clazz, String value) {
        return values(clazz).stream().anyMatch(i -> i.value().equals(value));
    }
    
    static boolean isRegistered(ITypeCode c) {
        return registry.containsKey(c.getClass()) && registry.get(c.getClass()).contains(c);
    }

    static boolean isNotRegistered(ITypeCode c) {
        return ! isRegistered(c);
    }
    
    @SuppressWarnings("unchecked")
    static <T extends ITypeCode> void register(T c) {
        Set<T> s = null;
        synchronized(registryLock) {
            s = (Set<T>) registry.get(c.getClass());
            if (s == null) {
                s = new HashSet<>();
                registry.put(c.getClass(), s);
            }
            s.add(c);
        }
    }
    
}
