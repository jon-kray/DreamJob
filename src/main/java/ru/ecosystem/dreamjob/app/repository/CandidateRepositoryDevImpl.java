package ru.ecosystem.dreamjob.app.repository;

import lombok.RequiredArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.ecosystem.dreamjob.app.config.Profiles;
import ru.ecosystem.dreamjob.app.model.Candidate;
import ru.ecosystem.dreamjob.app.repository.interfaces.CandidateRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ru.ecosystem.dreamjob.app.util.DreamJobUtils.*;

@Repository
@ThreadSafe
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Profile(Profiles.DEV)
public class CandidateRepositoryDevImpl implements CandidateRepository<Long, Candidate> {

    private final Map<Long, Candidate> candidates = new ConcurrentHashMap<>();

    private final WorkingModeRepositoryDevImpl workingModeRepositoryDevImpl;

    @PostConstruct
    @Override
    public void init() {
        long firstPostId = generateId();
        long secondPostId = generateId();
        long thirdPostId = generateId();
        candidates.put(firstPostId, new Candidate(firstPostId,
                "Junior Java Developer", "150 000", "description",
                workingModeRepositoryDevImpl.getById(1L), new byte[]{}, LocalDateTime.now()));
        candidates.put(secondPostId, new Candidate(secondPostId,
                "Middle Php Developer", "200 000", "description",
                workingModeRepositoryDevImpl.getById(2L), new byte[]{}, LocalDateTime.now()));
        candidates.put(thirdPostId, new Candidate(thirdPostId,
                "Senior DevOps", "256 000", "description",
                workingModeRepositoryDevImpl.getById(3L), new byte[]{}, LocalDateTime.now()));
    }

    @Override
    public List<Candidate> findAll() {
        return new ArrayList<>(candidates.values());
    }

    @Override
    public Candidate add(Candidate candidate) {
        long id = generateId();
        candidate.setId(id);
        candidate.setCreated(LocalDateTime.now());
        return candidates.putIfAbsent(id, candidate);
    }

    @Override
    public Candidate getById(Long id) {
        return candidates.get(id);
    }

    @Override
    public void update(Long id, Candidate candidate) {
        candidates.computeIfPresent(id, (idSaved, candidateUpdated) -> {
            candidateUpdated.setId(id);
            candidateUpdated.setCreated(candidateUpdated.getCreated());
            candidateUpdated.setName(candidate.getName());
            candidateUpdated.setPrice(candidate.getPrice());
            candidateUpdated.setDescription(candidate.getDescription());
            candidateUpdated.setWorkingMode(candidate.getWorkingMode());
            candidateUpdated.setPhoto(candidate.getPhoto());
            return candidateUpdated;
        });
    }

    @Override
    public void delete(Long id) {
        candidates.remove(id);
    }
}
