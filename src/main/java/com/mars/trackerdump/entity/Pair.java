package com.mars.trackerdump.entity;

import java.io.Serializable;

public class Pair<K, V> implements Serializable {

    K key = null;
    V val = null;

    public Pair() {
    }

    public Pair(K key, V val) {
        this.key = key;
        this.val = val;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getVal() {
        return val;
    }

    public void setVal(V val) {
        this.val = val;
    }
}
