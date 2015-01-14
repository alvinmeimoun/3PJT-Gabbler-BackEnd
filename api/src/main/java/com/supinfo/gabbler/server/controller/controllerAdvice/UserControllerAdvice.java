package com.supinfo.gabbler.server.controller.controllerAdvice;

import com.supinfo.gabbler.server.exception.AbstractApiException;
import com.supinfo.gabbler.server.exception.user.InvalidCredentialsException;
import com.supinfo.gabbler.server.exception.user.InvalidPasswordException;
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

    /*@ExceptionHandler(value = InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public InvalidCredentialsException invalidCredentials(InvalidCredentialsException exception) {
        return exception;
    }

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public UserAlreadyExistsException userAlreadyExists(UserAlreadyExistsException exception) {
        return exception;
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public UserNotFoundException userNotFound(UserNotFoundException exception) {
        return exception;
    }

    @ExceptionHandler(value = InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public InvalidPasswordException invalidPassword(InvalidPasswordException exception) {
        return exception;
    }*/

    @ExceptionHandler(value = AbstractApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String invalidPassword(AbstractApiException exception) {
        return exception.getClass().getName()+" : "+exception.getLocalizedMessage();
    }
}
