package org.jumpmind.pos.util.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class ITypeCodeRegistry {

    private static Map<Class<? extends ITypeCode>,Set<ITypeCode>> registry = new HashMap<>();
    private static final Object registryLock = new Object();


    public static Set<Class<? extends ITypeCode>> types() {
        return Collections.unmodifiableSet(registry.keySet());
    }
    
    public static Set<ITypeCode> values(Class<? extends ITypeCode> clazz) {
        return Collections.unmodifiableSet(registry.get(clazz));
    }
    
    static boolean isRegistered(ITypeCode c) {
        return registry.containsKey(c.getClass()) && registry.get(c.getClass()).contains(c);
    }

    static boolean isNotRegistered(ITypeCode c) {
        return ! isRegistered(c);
    }
    
    static void register(ITypeCode c) {
        Set<ITypeCode> s = null;
        synchronized(registryLock) {
            s = registry.get(c.getClass());
            if (s == null) {
                s = new HashSet<>();
                registry.put(c.getClass(), s);
            }
            s.add(c);
        }
    }
    
}
