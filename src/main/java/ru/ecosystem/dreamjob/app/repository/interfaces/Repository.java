package ru.ecosystem.dreamjob.app.repository.interfaces;

import java.util.List;

public interface Repository<K, V> {

    public static final String UNSUPPORTED_OPERATION_MESSAGE = "This operation is unsupported.";

    public default void init() {
        isUnsupported();
    }

    public static void isUnsupported() {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
    }

    public V add(V v);

    public List<V> findAll();

    public V getById(K k);

    public void update(K k, V v);

    public void delete(K k);
}
