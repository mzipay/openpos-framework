package org.jumpmind.jumppos.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class TypedMap implements Map<Object, Object> {
    private Map<Object, Object> delegate;

    public TypedMap(Map<Object, Object> delegate) {
        this.delegate = delegate;
    }

    public TypedMap() {
        this.delegate = new HashMap<Object, Object>();
    }

    @SuppressWarnings("unchecked")
    public <T> T get(TypedMapKey<T> key) {
        return (T) delegate.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T remove(TypedMapKey<T> key) {
        return (T) delegate.remove(key);
    }

    public <T> void put(TypedMapKey<T> key, T value) {
        delegate.put(key, value);
    }

    // --- Only calls to delegates below

    public void clear() {
        delegate.clear();
    }

    public boolean containsKey(Object key) {
        return delegate.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    public Set<java.util.Map.Entry<Object, Object>> entrySet() {
        return delegate.entrySet();
    }

    public boolean equals(Object o) {
        return delegate.equals(o);
    }

    public Object get(Object key) {
        return delegate.get(key);
    }

    public int hashCode() {
        return delegate.hashCode();
    }

    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    public Set<Object> keySet() {
        return delegate.keySet();
    }

    public Object put(Object key, Object value) {
        return delegate.put(key, value);
    }

    public void putAll(Map<? extends Object, ? extends Object> m) {
        delegate.putAll(m);
    }

    public Object remove(Object key) {
        return delegate.remove(key);
    }

    public int size() {
        return delegate.size();
    }

    public Collection<Object> values() {
        return delegate.values();
    }

    public static class TypedMapKey<T> {
        private String key;

        public TypedMapKey(String key) {
            this.key = key;
        }
        
        public static <T> TypedMapKey<T> of(Class<T> clazz, String key) {
            return new TypedMapKey<T>(key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            
            TypedMapKey<?> other = (TypedMapKey<?>) obj;
            return Objects.equals(this.key, other.key);
        }

        @Override
        public String toString() {
            return key;
        }
    }
}