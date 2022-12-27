package ru.ecosystem.dreamjob.app.controller;

import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockHttpSession;
import ru.ecosystem.dreamjob.app.dto.InfoDto;
import ru.ecosystem.dreamjob.app.dto.SignInDto;
import ru.ecosystem.dreamjob.app.dto.SignUpAdminDto;
import ru.ecosystem.dreamjob.app.ex.UnAuthorizedException;
import ru.ecosystem.dreamjob.app.repository.command.RegisterCommand;
import ru.ecosystem.dreamjob.app.repository.command.RegisterSuccessCommand;
import ru.ecosystem.dreamjob.app.repository.command.StatusRegister;
import ru.ecosystem.dreamjob.app.service.UserService;
import ru.ecosystem.dreamjob.app.util.DreamJobUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserService userService;

    @Mock
    private Map<StatusRegister, RegisterCommand> commandMap;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void registerStatusTestSuccess() {
        var adminDto = new SignUpAdminDto();
        adminDto.setPassword("password");
        adminDto.setUsername("username");
        adminDto.setSecretHash("secret");
        adminDto.setSecretNumber("111");
        Mockito.when(userService.registerAdmin(adminDto)).thenReturn(StatusRegister.REGISTER_SUCCESS);
        Mockito.when(commandMap.get(StatusRegister.REGISTER_SUCCESS)).thenReturn(new RegisterSuccessCommand());
        var rsl = authController.registerStatus(adminDto);

        Mockito.verify(userService, Mockito.times(1)).validationSecretParams("secret", "111");
        var infoDto = new InfoDto();
        infoDto.setMessage("Поздравляем с успешной регистрацией. Теперь, пожалуйста, авторизуйтесь.");
        Assertions.assertEquals(
                infoDto,
                rsl.getModel().get("info"));
    }

    @Test
    @SneakyThrows
    public void loginStatus() {
        var mockHttpSession = new MockHttpSession();
        var request = Mockito.mock(HttpServletRequest.class);
        var response = Mockito.mock(HttpServletResponse.class);
        var signInDto = new SignInDto();
        signInDto.setUsername("username");
        signInDto.setPassword("password");
        Mockito.when(userService.authorizationAdmin(signInDto)).thenReturn(Pair.of(Boolean.TRUE, 1L));
        Mockito.when(request.getSession()).thenReturn(mockHttpSession);
        Mockito.when(request.getContextPath()).thenReturn("http://localhost:3333");

        authController.loginStatus(signInDto, request, response);

        Assertions.assertEquals(1L, mockHttpSession.getAttribute(DreamJobUtils.USER_ID_KEY));
        Mockito.verify(response, Mockito.times(1)).sendRedirect("http://localhost:3333/hello");
    }

    @Test
    @SneakyThrows
    public void loginStatusFail() {
        var request = Mockito.mock(HttpServletRequest.class);
        var response = Mockito.mock(HttpServletResponse.class);
        var signInDto = new SignInDto();
        Mockito.when(userService.authorizationAdmin(ArgumentMatchers.any())).thenReturn(Pair.of(Boolean.FALSE, -1L));

        Assertions.assertThrows(UnAuthorizedException.class, () -> authController.loginStatus(signInDto, request, response));
    }
}