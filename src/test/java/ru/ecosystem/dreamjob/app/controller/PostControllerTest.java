package ru.ecosystem.dreamjob.app.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.ecosystem.dreamjob.app.model.Post;
import ru.ecosystem.dreamjob.app.service.PostService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

public class PostControllerTest {

    @InjectMocks
    private PostController postController;

    @Mock
    private PostService postService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @SneakyThrows
    public void addPostSubmitTest() {
        var request = Mockito.mock(HttpServletRequest.class);
        var response = Mockito.mock(HttpServletResponse.class);
        Mockito.when(request.getContextPath()).thenReturn("http://localhost:3333/test");
        var date = LocalDateTime.now();
        var postTest = Post.builder()
                .id(1L)
                .company("test")
                .description("test")
                .created(date)
                .build();
        postController.addPostSubmit(postTest, request, response);

        Mockito.verify(postService, Mockito.times(1)).addPost(postTest);
        Mockito.verify(response).sendRedirect("http://localhost:3333/test/posts");
    }

    @Test
    @SneakyThrows
    public void updatePostSubmit() {
        var request = Mockito.mock(HttpServletRequest.class);
        var response = Mockito.mock(HttpServletResponse.class);
        Mockito.when(request.getContextPath()).thenReturn("http://localhost:3333/test");
        var date = LocalDateTime.now();
        var postTest = Post.builder()
                .id(1L)
                .company("test")
                .description("test")
                .created(date)
                .build();
        postController.updatePostSubmit(postTest, 1L, request, response);

        Mockito.verify(postService, Mockito.times(1)).updatePost(1L, postTest);
        Mockito.verify(response).sendRedirect("http://localhost:3333/test/posts");
    }

}