package ru.ecosystem.dreamjob.app.repository.command;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import ru.ecosystem.dreamjob.app.dto.InfoDto;
import java.util.Map;

@Component("loginIsBusyCommand")
public class LoginIsBusyCommand implements RegisterCommand {

    @Override
    public ModelAndView execute() {
        var infoDto = new InfoDto();
        infoDto.setMessage("Данный логин занят. Пожалуйста, используйте другой логин.");
        return new ModelAndView("warning", Map.of("info", infoDto));
    }
}
