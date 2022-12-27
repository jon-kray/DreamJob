package ru.ecosystem.dreamjob.app.repository.command;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RegisterCommand {

    public ModelAndView execute();
}
