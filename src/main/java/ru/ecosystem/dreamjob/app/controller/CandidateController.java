package ru.ecosystem.dreamjob.app.controller;

import lombok.RequiredArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.ecosystem.dreamjob.app.model.Candidate;
import ru.ecosystem.dreamjob.app.model.Post;
import ru.ecosystem.dreamjob.app.model.WorkingMode;
import ru.ecosystem.dreamjob.app.service.CandidateService;
import ru.ecosystem.dreamjob.app.service.WorkingModeService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/candidates")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ThreadSafe
public class CandidateController {

    private final CandidateService candidateService;
    private final WorkingModeService workingModeService;

    @GetMapping
    public ModelAndView getAllCandidates() {
        return new ModelAndView("candidates", Map.of("candidates", candidateService.getAllCandidates()));
    }

    @GetMapping("/addCandidate")
    public ModelAndView addCandidateForm() {
        return new ModelAndView(
                "add-candidate",
                Map.of("candidate", new Candidate(),
                       "modes", workingModeService.findAll()));
    }

    @PostMapping("/addCandidate")
    public void addCandidateSubmit(@ModelAttribute("candidate") Candidate candidate,
                                   @RequestParam("file") MultipartFile multipartFile,
                                   HttpServletRequest httpServletRequest,
                                   HttpServletResponse httpServletResponse) throws IOException {

        var mode = workingModeService.getById(candidate.getWorkingMode().getId());
        candidate.setWorkingMode(mode);
        candidate.setPhoto(multipartFile.getBytes());
        candidateService.addCandidate(candidate);
        httpServletResponse.sendRedirect(String.format("%s/candidates", httpServletRequest.getContextPath()));
    }

    @GetMapping("/updateCandidate/{id}")
    public ModelAndView updateCandidateForm(@PathVariable("id") Long id) {
        var rsl = candidateService.getCandidateById(id);
        return new ModelAndView(
                "update-candidate",
                Map.of("candidate", rsl,
                        "modes", workingModeService.findAll())
        );
    }

    @GetMapping("/photoCandidate/{id}")
    public ResponseEntity<Resource> loadPhoto(@PathVariable("id") Long id) {
        var candidate = candidateService.getCandidateById(id);
        return ResponseEntity.ok()
                .headers(new HttpHeaders())
                .contentLength(candidate.getPhoto().length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(candidate.getPhoto()));
    }

    @PutMapping("/updateCandidate/{id}")
    public void updateCandidateSubmit(@ModelAttribute("candidate") Candidate candidate,
                                      @PathVariable("id") Long id,
                                      @RequestParam("file") MultipartFile multipartFile,
                                      HttpServletRequest httpServletRequest,
                                      HttpServletResponse httpServletResponse) throws IOException {

        candidate.setPhoto(multipartFile.getBytes());
        candidate.setWorkingMode(workingModeService.getById(candidate.getWorkingMode().getId()));
        candidateService.updateCandidate(id, candidate);
        httpServletResponse.sendRedirect(String.format("%s/candidates", httpServletRequest.getContextPath()));
    }

    @GetMapping("/deleteCandidate/{id}")
    public void deleteCandidate(@PathVariable("id") Long id,
                                HttpServletResponse httpServletResponse,
                                HttpServletRequest httpServletRequest) throws IOException {

        candidateService.deleteCandidate(id);
        httpServletResponse.sendRedirect(String.format("%s/candidates", httpServletRequest.getContextPath()));
    }
}
