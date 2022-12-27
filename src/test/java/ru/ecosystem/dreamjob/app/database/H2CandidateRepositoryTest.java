package ru.ecosystem.dreamjob.app.database;

import liquibase.integration.spring.SpringLiquibase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.ecosystem.dreamjob.app.config.Profiles;
import ru.ecosystem.dreamjob.app.model.Candidate;
import ru.ecosystem.dreamjob.app.model.WorkingMode;
import ru.ecosystem.dreamjob.app.repository.interfaces.CandidateRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@JdbcTest
@ActiveProfiles(Profiles.TEST)
@ContextConfiguration(classes = TestAppConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class H2CandidateRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @MockBean
    private SpringLiquibase springLiquibase;

    @Autowired
    private CandidateRepository<Long, Candidate> candidateRepository;

    @BeforeEach
    @SneakyThrows
    public void createTables() {

        var scriptCandidate = new BufferedReader(
                new FileReader("src/test/java/ru/ecosystem/dreamjob/app/scripts/candidate-table-test.sql"))
                .lines()
                .collect(Collectors.joining());

        var scriptWorkingModes = new BufferedReader(
                new FileReader("src/test/java/ru/ecosystem/dreamjob/app/scripts/working-modes-test.sql"))
                .lines()
                .collect(Collectors.joining());

        jdbcTemplate.execute(scriptWorkingModes);
        jdbcTemplate.execute(scriptCandidate);
    }

    @AfterEach
    public void dropTables() {
        jdbcTemplate.execute("DROP TABLE candidates");
        jdbcTemplate.execute("DROP TABLE working_modes");
    }


    @Test
    public void whenAddAndGetCandidate() {
        var candidate = new Candidate();
        candidate.setPhoto(new byte[]{});
        candidate.setCreated(LocalDateTime.now());
        candidate.setPrice("150.000 rub");
        candidate.setName("Junior Java Developer");
        candidate.setDescription("I am test object");
        candidate.setWorkingMode(WorkingMode.builder()
                        .id(1L)
                        .mode(WorkingMode.Type.REMOTELY)
                .build());
        var result = candidateRepository.add(candidate);
        Assertions.assertTrue(result.getId() != 0);

        var notNullCandidate = candidateRepository.getById(result.getId());
        Assertions.assertNotNull(notNullCandidate);
        Assertions.assertEquals("I am test object", notNullCandidate.getDescription());
    }

    @Test
    public void whenFindAllCandidates() {
        var candidateFirst = new Candidate();
        candidateFirst.setPhoto(new byte[]{});
        candidateFirst.setCreated(LocalDateTime.now());
        candidateFirst.setPrice("150.000 rub");
        candidateFirst.setName("Junior Java Developer");
        candidateFirst.setDescription("I am test object 1");
        candidateFirst.setWorkingMode(WorkingMode.builder()
                .id(1L)
                .mode(WorkingMode.Type.REMOTELY)
                .build());
        var candidateSecond = new Candidate();
        candidateSecond.setPhoto(new byte[]{});
        candidateSecond.setCreated(LocalDateTime.now());
        candidateSecond.setPrice("230.000 rub");
        candidateSecond.setName("Middle Java Developer");
        candidateSecond.setDescription("I am test object 2");
        candidateSecond.setWorkingMode(WorkingMode.builder()
                .id(2L)
                .mode(WorkingMode.Type.IN_THE_OFFICE)
                .build());

        List<Candidate> candidates = List.of(candidateFirst, candidateSecond);
        candidates.forEach(candidate -> candidateRepository.add(candidate));

        Assertions.assertEquals(2, candidateRepository.findAll().size());
    }
}
