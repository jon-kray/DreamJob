package ru.ecosystem.dreamjob.app.repository.command;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import ru.ecosystem.dreamjob.app.dto.InfoDto;

import java.util.Map;

@Component("registerSuccessCommand")
public class RegisterSuccessCommand implements RegisterCommand {

    @Override
    public ModelAndView execute() {
        var info = new InfoDto();
        info.setMessage("Поздравляем с успешной регистрацией. Теперь, пожалуйста, авторизуйтесь.");
        return new ModelAndView("success", Map.of("info", info));
    }
}
