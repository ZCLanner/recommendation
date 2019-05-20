package me.lanner.liz.paper.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SortUtils {

    private SortUtils() {
        throw new UnsupportedOperationException();
    }

    public static <K, V extends Comparable<V>> List<Map.Entry<K, V>> sortByValueDesc(Map<K, V> map) {
        List<Map.Entry<K, V>> entries = new ArrayList<>(map.entrySet());
        entries.sort((e1, e2) -> 1-e1.getValue().compareTo(e2.getValue()));
        return entries;
    }

}
