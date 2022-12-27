package ru.ecosystem.dreamjob.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MainController {

    @GetMapping("/hello")
    public ModelAndView welcome() {
        return new ModelAndView("main");
    }
}
