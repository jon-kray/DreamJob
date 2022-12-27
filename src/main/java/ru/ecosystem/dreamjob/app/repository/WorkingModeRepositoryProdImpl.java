package ru.ecosystem.dreamjob.app.repository;

import lombok.RequiredArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.ecosystem.dreamjob.app.config.Profiles;
import ru.ecosystem.dreamjob.app.model.WorkingMode;
import ru.ecosystem.dreamjob.app.repository.interfaces.WorkingModeRepository;
import ru.ecosystem.dreamjob.app.mapper.WorkingModeRowMapper;

import java.util.List;

import static ru.ecosystem.dreamjob.app.repository.interfaces.Repository.isUnsupported;

@Repository
@ThreadSafe
@Profile(Profiles.PROD)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkingModeRepositoryProdImpl implements WorkingModeRepository<Long, WorkingMode> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public WorkingMode add(WorkingMode workingMode) {
        isUnsupported();
        return null;
    }

    @Override
    public List<WorkingMode> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, new WorkingModeRowMapper());
    }

    @Override
    public WorkingMode getById(Long id) {
        return jdbcTemplate.queryForObject(FIND_BY_ID_QUERY, new WorkingModeRowMapper(), id);
    }

    @Override
    public void update(Long id, WorkingMode workingMode) {
        isUnsupported();
    }

    @Override
    public void delete(Long id) {
        isUnsupported();
    }

    private static final String FIND_ALL_QUERY = "SELECT * FROM working_modes";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM working_modes WHERE id = ?";
}
