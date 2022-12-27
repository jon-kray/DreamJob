package ru.ecosystem.dreamjob.app.service;

import lombok.RequiredArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ecosystem.dreamjob.app.model.Candidate;
import ru.ecosystem.dreamjob.app.repository.CandidateRepositoryDevImpl;
import ru.ecosystem.dreamjob.app.repository.interfaces.CandidateRepository;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ThreadSafe
public class CandidateService {

    private final CandidateRepository<Long, Candidate> candidateRepository;

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public void addCandidate(Candidate candidate) {
        candidateRepository.add(candidate);
    }

    public Candidate getCandidateById(Long id) {
        return candidateRepository.getById(id);
    }

    public void updateCandidate(Long id, Candidate candidate) {
        candidateRepository.update(id, candidate);
    }

    public void deleteCandidate(Long id) {
        candidateRepository.delete(id);
    }
}
