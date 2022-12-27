package ru.ecosystem.dreamjob.app.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.ecosystem.dreamjob.app.config.Profiles;
import ru.ecosystem.dreamjob.app.model.WorkingMode;
import ru.ecosystem.dreamjob.app.repository.interfaces.WorkingModeRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ru.ecosystem.dreamjob.app.repository.interfaces.Repository.isUnsupported;

@ThreadSafe
@Repository
@Profile(Profiles.DEV)
public class WorkingModeRepositoryDevImpl implements WorkingModeRepository<Long, WorkingMode> {

    private final Map<Long, WorkingMode> modes = new ConcurrentHashMap<>();

    @PostConstruct
    @Override
    public void init() {
        modes.put(1L, new WorkingMode(1L, WorkingMode.Type.HYBRID));
        modes.put(2L, new WorkingMode(2L, WorkingMode.Type.IN_THE_OFFICE));
        modes.put(3L, new WorkingMode(3L, WorkingMode.Type.REMOTELY));
    }

    @Override
    public WorkingMode add(WorkingMode workingMode) {
        isUnsupported();
        return null;
    }

    @Override
    public WorkingMode getById(Long id) {
        return modes.get(id);
    }

    @Override
    public void update(Long id, WorkingMode workingMode) {
        isUnsupported();
    }

    @Override
    public void delete(Long id) {
        isUnsupported();
    }


    @Override
    public List<WorkingMode> findAll() {
        return new ArrayList<>(modes.values());
    }
}
