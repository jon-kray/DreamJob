package ru.ecosystem.dreamjob.app.repository;

import lombok.RequiredArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.ecosystem.dreamjob.app.config.Profiles;
import ru.ecosystem.dreamjob.app.ex.UnAuthorizedException;
import ru.ecosystem.dreamjob.app.model.User;
import ru.ecosystem.dreamjob.app.repository.interfaces.UserRepository;
import ru.ecosystem.dreamjob.app.util.DreamJobUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ru.ecosystem.dreamjob.app.repository.interfaces.Repository.isUnsupported;

@Repository
@ThreadSafe
@Profile(Profiles.DEV)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserRepositoryDevImpl implements UserRepository<Long, User> {

    private final Map<Long, User> users = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        users.put(1L, new User(1L, "nik228", "krutoooiii111"));
    }

    @Override
    public User add(User user) {
        long id = DreamJobUtils.generateId();
        user.setId(id);
        return users.putIfAbsent(id, user);
    }

    @Override
    public List<User> findAll() {
        isUnsupported();
        return null;
    }

    @Override
    public User getById(Long id) {
        return users.get(id);
    }

    @Override
    public void update(Long id, User user) {
        isUnsupported();
    }

    @Override
    public void delete(Long id) {
        isUnsupported();
    }

    public boolean existsUserByUsername(String username) {
        return users.values()
                .stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    public Pair<Boolean, Long> existsUserFullyAuth(String username, String password) {
        return users.values()
                .stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .map(user -> Pair.of(Boolean.TRUE, user.getId()))
                .findFirst()
                .orElseGet(() -> Pair.of(Boolean.FALSE, -1L));
    }
}
