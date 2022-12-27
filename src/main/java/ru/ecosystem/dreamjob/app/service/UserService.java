package ru.ecosystem.dreamjob.app.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.ecosystem.dreamjob.app.dto.SignInDto;
import ru.ecosystem.dreamjob.app.dto.SignUpAdminDto;
import ru.ecosystem.dreamjob.app.ex.ForbiddenException;
import ru.ecosystem.dreamjob.app.model.User;
import ru.ecosystem.dreamjob.app.repository.command.StatusRegister;
import ru.ecosystem.dreamjob.app.repository.interfaces.UserRepository;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    @Value("${admin.secret.hash}")
    private String hashSecret;
    @Value("${admin.secret.number}")
    private String numberSecret;

    private final UserRepository<Long, User> userRepository;

    public Pair<Boolean, Long> authorizationAdmin(SignInDto signInDto) {
        return userRepository.existsUserFullyAuth(
                signInDto.getUsername(),
                signInDto.getPassword());
    }

    public StatusRegister registerAdmin(SignUpAdminDto signUpAdminDto) {
        if (userRepository.existsUserByUsername(signUpAdminDto.getUsername())) {
            return StatusRegister.LOGIN_IS_BUSY;
        } else if (userRepository.existsUserFullyAuth(signUpAdminDto.getUsername(), signUpAdminDto.getPassword()).getLeft()) {
            return StatusRegister.ALREADY_REGISTER;
        } else {
            User user = new User();
            user.setUsername(signUpAdminDto.getUsername());
            user.setPassword(signUpAdminDto.getPassword());
            userRepository.add(user);
            return StatusRegister.REGISTER_SUCCESS;
        }
    }

    public void validationSecretParams(String hashSecret, String numberSecret) {
        if (!(this.hashSecret.equals(hashSecret) || this.numberSecret.equals(numberSecret))) {
            throw new ForbiddenException("Неверные секретные параметры для регистрации в роли администратора.");
        }
    }
}
