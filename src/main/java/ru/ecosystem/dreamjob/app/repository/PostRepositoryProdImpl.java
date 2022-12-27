package ru.ecosystem.dreamjob.app.repository;

import lombok.RequiredArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.ecosystem.dreamjob.app.config.Profiles;
import ru.ecosystem.dreamjob.app.model.Post;
import ru.ecosystem.dreamjob.app.repository.interfaces.PostRepository;

import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Repository
@ThreadSafe
@Profile(Profiles.PROD)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PostRepositoryProdImpl implements PostRepository<Long, Post> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Post add(Post post) {
        post.setCreated(LocalDateTime.now());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, post.getName());
            ps.setString(2, post.getCompany());
            ps.setString(3, post.getDescription());
            ps.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            return ps;
        }, keyHolder);
        var id = (Long) requireNonNull(keyHolder.getKeys()).get("id");
        post.setId(id);
        return post;
    }

    @Override
    public List<Post> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, new BeanPropertyRowMapper<>(Post.class));
    }

    @Override
    public Post getById(Long id) {
        return jdbcTemplate.queryForObject(FIND_BY_ID_QUERY, new BeanPropertyRowMapper<>(Post.class), id);
    }

    @Override
    public void update(Long id, Post post) {
        jdbcTemplate.update(UPDATE_BY_ID_QUERY, post.getName(), post.getCompany(), post.getDescription(), id);
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_BY_ID_QUERY, id);
    }

    private static final String INSERT_QUERY = "INSERT INTO POSTS(name, company, description, created) " +
                                               "VALUES (?, ?, ?, ?)";
    private static final String FIND_ALL_QUERY = "SELECT * FROM posts";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM posts WHERE id = ?";
    private static final String UPDATE_BY_ID_QUERY = "UPDATE posts " +
                                                     "SET name = ?, company = ?, description = ?" +
                                                     "WHERE id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM posts WHERE id = ?";

}
