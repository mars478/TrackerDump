package com.mars.trackerdump.db.common;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("override")
public abstract class MapProxy implements Map, Serializable {

    public abstract Object get(Object key);

    public abstract Object put(Object key, Object value);

    // <editor-fold defaultstate="collapsed" desc="deprecated">
    @Deprecated
    public boolean isEmpty() {
        return true;
    }

    @Deprecated
    public boolean containsKey(Object key) {
        return false;
    }

    @Deprecated
    public boolean containsValue(Object value) {
        return false;
    }

    @Deprecated
    public Object remove(Object key) {
        return null;
    }

    @Deprecated
    public void putAll(Map m) {
    }

    @Deprecated
    public void clear() {
    }

    @Deprecated
    public Set keySet() {
        return null;
    }

    @Deprecated
    public Collection values() {
        return null;
    }

    @Deprecated
    public Set entrySet() {
        return null;
    }

    @Deprecated
    public int size() {
        return 0;
    }
    // </editor-fold>
}
