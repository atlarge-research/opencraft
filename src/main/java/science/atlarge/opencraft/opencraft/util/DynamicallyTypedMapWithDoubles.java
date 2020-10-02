package science.atlarge.opencraft.opencraft.util;

public interface DynamicallyTypedMapWithDoubles<K> extends DynamicallyTypedMapWithFloats<K> {

    /**
     * Retrieves an entry as a {@code double}.
     *
     * @param key the key to look up
     * @return the value as a {@code double}
     */
    double getDouble(K key);
}
