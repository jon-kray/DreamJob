package ru.ecosystem.dreamjob.app.repository;

import lombok.RequiredArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.ecosystem.dreamjob.app.config.Profiles;
import ru.ecosystem.dreamjob.app.mapper.CandidateRowMapper;
import ru.ecosystem.dreamjob.app.model.Candidate;
import ru.ecosystem.dreamjob.app.repository.interfaces.CandidateRepository;

import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Repository
@ThreadSafe
@Profile(Profiles.PROD)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CandidateRepositoryProdImpl implements CandidateRepository<Long, Candidate> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Candidate add(Candidate candidate) {
        candidate.setCreated(LocalDateTime.now());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getPrice());
            ps.setString(3, candidate.getDescription());
            ps.setLong(4, candidate.getWorkingMode().getId());
            ps.setBytes(5, candidate.getPhoto());
            ps.setTimestamp(6, Timestamp.valueOf(candidate.getCreated()));
            return ps;
        }, keyHolder);
        var id = (Long) requireNonNull(keyHolder.getKeys()).get("id");
        candidate.setId(id);
        return candidate;
    }

    @Override
    public List<Candidate> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, new CandidateRowMapper());
    }

    @Override
    public Candidate getById(Long id) {
        return jdbcTemplate.queryForObject(FIND_BY_ID_QUERY, new CandidateRowMapper(), id);
    }

    @Override
    public void update(Long id, Candidate candidate) {
        jdbcTemplate.update(UPDATE_BY_ID_QUERY, candidate.getName(), candidate.getPrice(), candidate.getDescription(),
                            candidate.getWorkingMode().getId(), candidate.getPhoto(), id);
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_BY_ID_QUERY, id);
    }

    private static final String INSERT_QUERY = "INSERT INTO candidates" +
                                               "(name, price, description, working_mode_id, photo, created) " +
                                               "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String FIND_ALL_QUERY = "SELECT candidates.id as candidate_id, candidates.name as candidate_name, " +
                                                 "candidates.price as candidates_price, candidates.description as candidates_description, " +
                                                 "candidates.photo as candidate_photo, candidates.created as candidate_created, " +
                                                 "working_modes.id as working_modes_id, working_modes.mode as working_modes_mode " +
                                                 "FROM candidates " +
                                                 "INNER JOIN working_modes " +
                                                 "ON candidates.working_mode_id = working_modes.id";
    private static final String FIND_BY_ID_QUERY = "SELECT candidates.id as candidate_id, candidates.name as candidate_name, " +
                                                   "candidates.price as candidates_price, candidates.description as candidates_description, " +
                                                   "candidates.photo as candidate_photo, candidates.created as candidate_created, " +
                                                   "working_modes.id as working_modes_id, working_modes.mode as working_modes_mode " +
                                                   "FROM candidates " +
                                                   "INNER JOIN working_modes " +
                                                   "ON candidates.working_mode_id = working_modes.id " +
                                                   "WHERE candidates.id = ?";
    private static final String UPDATE_BY_ID_QUERY = "UPDATE candidates " +
                                                     "SET name = ?, price = ?, description = ?," +
                                                     " working_mode_id = ?, photo = ?" +
                                                     "WHERE id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM candidates WHERE id = ?";
}
