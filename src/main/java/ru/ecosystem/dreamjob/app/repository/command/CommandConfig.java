package ru.ecosystem.dreamjob.app.repository.command;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CommandConfig {

    @Bean("commandsRegistration")
    public Map<StatusRegister, RegisterCommand> commandMap(AlreadyRegisterCommand alreadyRegisterCommand,
                                                           RegisterSuccessCommand registerSuccessCommand,
                                                           LoginIsBusyCommand loginIsBusyCommand) {
        final Map<StatusRegister, RegisterCommand> map = new HashMap<>();
        map.put(StatusRegister.ALREADY_REGISTER, alreadyRegisterCommand);
        map.put(StatusRegister.REGISTER_SUCCESS, registerSuccessCommand);
        map.put(StatusRegister.LOGIN_IS_BUSY, loginIsBusyCommand);
        return map;
    }
}
