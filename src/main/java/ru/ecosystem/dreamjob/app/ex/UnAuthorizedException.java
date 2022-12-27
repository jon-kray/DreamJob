package ru.ecosystem.dreamjob.app.ex;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends RuntimeException {

    @Getter
    private final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

    public UnAuthorizedException(String message) {
        super(message);
    }
}
