package ru.ecosystem.dreamjob.app.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.ecosystem.dreamjob.app.config.Profiles;
import ru.ecosystem.dreamjob.app.model.Post;
import ru.ecosystem.dreamjob.app.repository.interfaces.PostRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static ru.ecosystem.dreamjob.app.util.DreamJobUtils.*;

@Repository
@ThreadSafe
@Profile(Profiles.DEV)
public class PostRepositoryDevImpl implements PostRepository<Long, Post> {

    private final Map<Long, Post> posts = new ConcurrentHashMap<>();

    @PostConstruct
    @Override
    public void init() {
        long firstPostId = generateId();
        long secondPostId = generateId();
        long thirdPostId = generateId();
        posts.put(firstPostId, new Post(firstPostId, "Junior Java Developer", "company", "description", LocalDateTime.now()));
        posts.put(secondPostId, new Post(secondPostId, "Middle Java Developer", "company", "description", LocalDateTime.now()));
        posts.put(thirdPostId, new Post(thirdPostId, "Senior Java Developer", "company", "description", LocalDateTime.now()));
    }

    public List<Post> findAll() {
        return new ArrayList<>(posts.values());
    }

    public Post add(Post post) {
        long id = generateId();
        post.setId(id);
        post.setCreated(LocalDateTime.now());
        return posts.putIfAbsent(id, post);
    }

    public void update(Long id, Post post) {
        posts.computeIfPresent(id, (idSaved, postUpdated) -> {
            postUpdated.setId(id);
            postUpdated.setCreated(postUpdated.getCreated());
            postUpdated.setCompany(post.getCompany());
            postUpdated.setName(post.getName());
            postUpdated.setDescription(post.getDescription());
            return postUpdated;
        });
    }

    @Override
    public void delete(Long id) {
        posts.remove(id);
    }

    public Post getById(Long id) {
        return posts.get(id);
    }
}
