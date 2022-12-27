package ru.ecosystem.dreamjob.app.database;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.ecosystem.dreamjob.app.repository.CandidateRepositoryProdImpl;
import ru.ecosystem.dreamjob.app.repository.PostRepositoryProdImpl;

@Configuration
public class TestAppConfiguration {

    @Bean
    public CandidateRepositoryProdImpl candidateRepositoryProd(JdbcTemplate jdbcTemplate) {
        return new CandidateRepositoryProdImpl(jdbcTemplate);
    }

    @Bean
    public PostRepositoryProdImpl postRepositoryProd(JdbcTemplate jdbcTemplate) {
        return new PostRepositoryProdImpl(jdbcTemplate);
    }
}
