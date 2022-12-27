package ru.ecosystem.dreamjob.app.ex;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends RuntimeException {

    @Getter
    private final HttpStatus httpStatus = HttpStatus.FORBIDDEN;

    public ForbiddenException(String message) {
        super(message);
    }
}
