package ru.ecosystem.dreamjob.app.database;

import liquibase.integration.spring.SpringLiquibase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.ecosystem.dreamjob.app.config.Profiles;
import ru.ecosystem.dreamjob.app.model.Post;
import ru.ecosystem.dreamjob.app.repository.PostRepositoryProdImpl;
import ru.ecosystem.dreamjob.app.repository.interfaces.PostRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@JdbcTest
@ActiveProfiles(Profiles.TEST)
@ContextConfiguration(classes = TestAppConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class H2PostRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @MockBean
    private SpringLiquibase springLiquibase;

    @Autowired
    private PostRepository<Long, Post> postRepository;


    @BeforeEach
    @SneakyThrows
    public void createTables() {

        var scriptPosts = new BufferedReader(
                new FileReader("src/test/java/ru/ecosystem/dreamjob/app/scripts/post-table-test.sql"))
                .lines()
                .collect(Collectors.joining());

        jdbcTemplate.execute(scriptPosts);
    }

    @AfterEach
    public void dropTables() {
        jdbcTemplate.execute("DROP TABLE posts");
    }

    @Test
    public void whenAddAndGetPost() {
        var post = new Post();
        post.setCreated(LocalDateTime.now());
        post.setDescription("I am test object");
        post.setCompany("PAO Sberbank");
        post.setName("Senior DevOps");
        var result = postRepository.add(post);
        Assertions.assertTrue(result.getId() != 0);

        var notNullPost = postRepository.getById(result.getId());
        Assertions.assertNotNull(notNullPost);

        Assertions.assertEquals("I am test object", notNullPost.getDescription());
    }

    @Test
    public void whenFindAllPosts() {
        var postFirst = new Post();
        postFirst.setCreated(LocalDateTime.now());
        postFirst.setDescription("I am test object 1");
        postFirst.setCompany("PAO Sberbank");
        postFirst.setName("Senior DevOps");

        var postSecond = new Post();
        postSecond.setCreated(LocalDateTime.now());
        postSecond.setDescription("I am test object 2");
        postSecond.setCompany("Digital Economy League");
        postSecond.setName("Senior Java Developer");

        List<Post> posts = List.of(postFirst, postSecond);

        posts.forEach(post -> postRepository.add(post));

        var result = postRepository.findAll();

        Assertions.assertEquals(2, result.size());
    }

}
