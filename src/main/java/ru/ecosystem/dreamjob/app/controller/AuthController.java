package ru.ecosystem.dreamjob.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import ru.ecosystem.dreamjob.app.dto.InfoDto;
import ru.ecosystem.dreamjob.app.dto.SignInDto;
import ru.ecosystem.dreamjob.app.dto.SignUpAdminDto;
import ru.ecosystem.dreamjob.app.ex.ForbiddenException;
import ru.ecosystem.dreamjob.app.ex.UnAuthorizedException;
import ru.ecosystem.dreamjob.app.repository.command.RegisterCommand;
import ru.ecosystem.dreamjob.app.repository.command.StatusRegister;
import ru.ecosystem.dreamjob.app.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final Map<StatusRegister, RegisterCommand> commandMap;

    public AuthController(UserService userService,
                          @Qualifier("commandsRegistration") Map<StatusRegister, RegisterCommand> commandMap) {

        this.userService = userService;
        this.commandMap = commandMap;
    }

    @GetMapping("/register/admin")
    public ModelAndView registerAdmin() {
        return new ModelAndView("register", Map.of("signup", new SignUpAdminDto()));
    }

    @PostMapping("/register/admin")
    public ModelAndView registerStatus(@ModelAttribute("signup") SignUpAdminDto signUpAdminDto) {

        userService.validationSecretParams(signUpAdminDto.getSecretHash(), signUpAdminDto.getSecretNumber());
        var status = userService.registerAdmin(signUpAdminDto);

        return commandMap.get(status).execute();
    }

    @GetMapping("/login")
    public ModelAndView loginAdmin() {
        return new ModelAndView("login", Map.of("signin", new SignInDto()));
    }

    @PostMapping("/login")
    public void loginStatus(@ModelAttribute("signin") SignInDto signInDto,
                            HttpServletRequest httpServletRequest,
                            HttpServletResponse httpServletResponse) throws IOException {

        var result = userService.authorizationAdmin(signInDto);

        if (result.getLeft()) {
            var httpSession = httpServletRequest.getSession();
            httpSession.setAttribute("USER_ID", result.getRight());
            httpServletResponse.sendRedirect(String.format("%s/hello", httpServletRequest.getContextPath()));
        } else {
            throw new UnAuthorizedException("Неверный логин или пароль");
        }
    }

    @GetMapping("/logout")
    public void logoutStatus(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse) throws IOException {

        httpServletRequest.getSession().invalidate();
        httpServletResponse.sendRedirect(String.format("%s/auth/login", httpServletRequest.getContextPath()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ModelAndView exceptionHandlerAccessDenied(ForbiddenException forbiddenException) {
        return new ModelAndView(
                "error",
                Map.of("info", new InfoDto(forbiddenException.getMessage())),
                forbiddenException.getHttpStatus());
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ModelAndView exceptionHandlerDuplicateLogin(UnAuthorizedException unAuthorizedException) {
        return new ModelAndView(
                "warning",
                Map.of("info", new InfoDto(unAuthorizedException.getMessage())),
                unAuthorizedException.getHttpStatus());
    }
}
