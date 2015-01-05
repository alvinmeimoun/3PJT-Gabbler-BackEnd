package com.supinfo.gabbler.server.controller.controllerAdvice;

import com.supinfo.gabbler.server.exception.user.InvalidCredentialsException;
import com.supinfo.gabbler.server.exception.user.UserAlreadyExistsException;
import com.supinfo.gabbler.server.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class UserControllerAdvice {

    @ExceptionHandler(value = InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public InvalidCredentialsException userNotFound(InvalidCredentialsException exception) {
        return exception;
    }

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public UserAlreadyExistsException userNotFound(UserAlreadyExistsException exception) {
        return exception;
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public UserNotFoundException userNotFound(UserNotFoundException exception) {
        return exception;
    }
}
