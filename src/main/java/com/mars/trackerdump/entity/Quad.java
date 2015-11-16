package com.mars.trackerdump.entity;

import java.io.Serializable;

public class Quad<K, V, T, Q> extends Triad<K, V, T> implements Serializable {

    Q quad = null;

    public Quad() {
    }

    public Quad(K key, V val, T third, Q quad) {
        super(key, val, third);
        this.quad = quad;
    }

    public Q getQuad() {
        return quad;
    }

    public void setQuad(Q quad) {
        this.quad = quad;
    }
}
