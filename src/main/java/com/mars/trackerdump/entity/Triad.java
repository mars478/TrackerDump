package com.mars.trackerdump.entity;

import com.mars.trackerdump.db.common.Pair;
import java.io.Serializable;

public class Triad<K, V, T> extends Pair<K, V> implements Serializable {

    T third = null;

    public Triad() {
    }

    public Triad(K key, V val, T third) {
        super(key, val);
        this.third = third;
    }

    public T getThird() {
        return third;
    }

    public void setThird(T third) {
        this.third = third;
    }

}
