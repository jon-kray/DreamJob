package ru.ecosystem.dreamjob.app.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import ru.ecosystem.dreamjob.app.model.Candidate;
import ru.ecosystem.dreamjob.app.model.WorkingMode;
import ru.ecosystem.dreamjob.app.service.CandidateService;
import ru.ecosystem.dreamjob.app.service.WorkingModeService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CandidateControllerTest {

    @InjectMocks
    private CandidateController candidateController;

    @Mock
    private CandidateService candidateService;

    @Mock
    private WorkingModeService workingModeService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @SneakyThrows
    public void addCandidateTest() {
        var file = Mockito.mock(MultipartFile.class);
        var request = Mockito.mock(HttpServletRequest.class);
        var response = Mockito.mock(HttpServletResponse.class);
        var candidate = new Candidate();
        candidate.setDescription("desc");
        candidate.setName("name");
        candidate.setCreated(LocalDateTime.now());
        candidate.setPrice("20 000 RUB");
        candidate.setWorkingMode(WorkingMode.builder()
                        .id(1L)
                .build());
        Mockito.when(workingModeService.getById(1L)).thenReturn(WorkingMode.builder()
                        .id(1L)
                        .mode(WorkingMode.Type.IN_THE_OFFICE)
                .build());
        Mockito.when(file.getBytes()).thenReturn(new byte[]{25, 34, 57});
        Mockito.when(request.getContextPath()).thenReturn("http://localhost:3333");

        candidateController.addCandidateSubmit(candidate, file, request, response);

        Mockito.verify(candidateService, Mockito.times(1)).addCandidate(candidate);
        Mockito.verify(response).sendRedirect("http://localhost:3333/candidates");
    }


    @Test
    @SneakyThrows
    public void loadPhoto() {
        Mockito.when(candidateService.getCandidateById(1L)).thenReturn(
                Candidate.builder()
                        .id(1L)
                        .workingMode(WorkingMode.builder()
                                .id(1L)
                                .mode(WorkingMode.Type.IN_THE_OFFICE)
                                .build())
                        .photo(new byte[]{25, 34, 50, 25})
                        .description("desc")
                        .name("name")
                        .created(LocalDateTime.now())
                        .build()
        );

        var rsl = candidateController.loadPhoto(1L);
        Assertions.assertEquals(HttpStatus.OK, rsl.getStatusCode());
        Assertions.assertEquals(4, rsl.getBody().contentLength());
    }
}