package io.github.lsrcz.kerasgui.layers;

import java.io.Serializable;

/**
 * An util class for pairs.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Sirui Lu
 */
public class Pair<K, V> implements Serializable {
    private K key;
    private V value;

    /**
     * Construct an Pair from the key and value.
     * @param key The key.
     * @param value The value.
     */
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Get the key.
     * @return The key.
     */
    public K getKey() {
        return key;
    }

    /**
     * Get the value.
     * @return The value.
     */
    public V getValue() {
        return value;
    }
}
