package ru.ecosystem.dreamjob.app.repository.interfaces;

import org.apache.commons.lang3.tuple.Pair;

public interface UserRepository<K, V> extends Repository<K, V> {

    public boolean existsUserByUsername(String username);

    public Pair<Boolean, Long> existsUserFullyAuth(String username, String password);
}
