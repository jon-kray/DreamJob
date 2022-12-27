package ru.ecosystem.dreamjob.app.repository;

import lombok.RequiredArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.ecosystem.dreamjob.app.config.Profiles;
import ru.ecosystem.dreamjob.app.mapper.PairRowMapper;
import ru.ecosystem.dreamjob.app.model.User;
import ru.ecosystem.dreamjob.app.repository.interfaces.UserRepository;

import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static ru.ecosystem.dreamjob.app.repository.interfaces.Repository.isUnsupported;

@Repository
@ThreadSafe
@Profile(Profiles.PROD)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserRepositoryProdImpl implements UserRepository<Long, User> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User add(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            return ps;
        }, keyHolder);
        var id = (Long) requireNonNull(keyHolder.getKeys()).get("id");
        user.setId(id);
        return user;
    }

    @Override
    public List<User> findAll() {
        isUnsupported();
        return null;
    }

    @Override
    public User getById(Long aLong) {
        isUnsupported();
        return null;
    }

    @Override
    public void update(Long aLong, User user) {
        isUnsupported();
    }

    @Override
    public void delete(Long aLong) {
        isUnsupported();
    }

    @Override
    public boolean existsUserByUsername(String username) {
        return Objects.requireNonNull(jdbcTemplate.queryForObject(EXISTS_BY_USERNAME, Long.class, username)) == 1;
    }

    @Override
    public Pair<Boolean, Long> existsUserFullyAuth(String username, String password) {
        try {
            return jdbcTemplate.queryForObject(EXISTS_USER_FULLY_AUTH_QUERY, new PairRowMapper(), username, password);
        } catch (EmptyResultDataAccessException e) {
            return Pair.of(Boolean.FALSE, -1L);
        }
    }

    private static final String EXISTS_USER_FULLY_AUTH_QUERY = "SELECT id FROM users WHERE login = ? AND password = ?";

    private static final String EXISTS_BY_USERNAME = "SELECT count(*) FROM users WHERE login = ?";

    private static final String INSERT_QUERY = "INSERT INTO users(login, password) VALUES (?, ?)";
}
