package me.lanner.liz.paper.math;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Vector<K, V> {

    private final int length;
    private Map<K, V> valHolder = new HashMap<>();

    public Vector(int length) {
        this.length = length;
    }

    public int length() {
        return this.length;
    }

    public void set(K index, V v) {
        valHolder.put(index, v);
    }

    public V get(K index) {
        return valHolder.get(index);
    }

    public Set<K> allX() {
        return valHolder.keySet();
    }

}
