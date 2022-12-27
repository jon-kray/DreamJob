package ru.ecosystem.dreamjob.app.repository.command;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import ru.ecosystem.dreamjob.app.dto.InfoDto;


import java.util.Map;

@Component("alreadyRegisterCommand")
public class AlreadyRegisterCommand implements RegisterCommand {

    @Override
    public ModelAndView execute() {
        var infoDto = new InfoDto();
        infoDto.setMessage("Вы уже зарегистрированы. Авторизуйтесь пожалуйста.");
        return new ModelAndView("success", Map.of("info", infoDto));
    }
}
