package me.lanner.liz.paper.math;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Vector<V> {

    private final int length;
    private Map<Integer, V> valHolder = new HashMap<>();

    public Vector(int length) {
        this.length = length;
    }

    public int length() {
        return this.length;
    }

    public void set(Integer index, V v) {
        valHolder.put(index, v);
    }

    public V get(Integer index) {
        return valHolder.get(index);
    }

    public Set<Integer> allX() {
        return valHolder.keySet();
    }

}
