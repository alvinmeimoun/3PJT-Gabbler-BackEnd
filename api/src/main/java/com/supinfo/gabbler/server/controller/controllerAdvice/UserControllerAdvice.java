package com.supinfo.gabbler.server.controller.controllerAdvice;

import com.supinfo.gabbler.server.exception.AbstractApiException;
import com.supinfo.gabbler.server.exception.login.OperationNotAllowedException;
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

    @ExceptionHandler(value = AbstractApiException.class)
     @ResponseStatus(HttpStatus.BAD_REQUEST)
     public String invalidPassword(AbstractApiException exception) {
        return exception.getClass().getName()+" : "+exception.getLocalizedMessage();
    }

    @ExceptionHandler(value = OperationNotAllowedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String operationNotAllowed(OperationNotAllowedException exception) {
        return exception.getClass().getName()+" : "+exception.getLocalizedMessage();
    }
}
