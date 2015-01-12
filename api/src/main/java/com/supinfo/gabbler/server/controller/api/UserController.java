package com.supinfo.gabbler.server.controller.api;

import com.supinfo.gabbler.server.dto.ChangePassword;
import com.supinfo.gabbler.server.dto.Subscription;
import com.supinfo.gabbler.server.exception.login.InvalidTokenException;
import com.supinfo.gabbler.server.exception.user.InvalidCredentialsException;
import com.supinfo.gabbler.server.exception.user.InvalidPasswordException;
import com.supinfo.gabbler.server.exception.user.UserAlreadyExistsException;
import com.supinfo.gabbler.server.exception.user.UserNotFoundException;
import com.supinfo.gabbler.server.service.UserService;
import com.supinfo.gabbler.server.utils.AuthHeaderUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Alvin Meimoun
 */
@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    @Resource
    UserService userService;

    @RequestMapping(value = "/subscribe", method = RequestMethod.POST)
    public Long subscribe(@RequestBody Subscription subscription) throws UserAlreadyExistsException {
        return userService.subscribe(subscription).getId();
    }

    @RequestMapping(value = "/password_change", method = RequestMethod.POST)
    public void changePassword(@RequestHeader(value = AuthHeaderUtil.TOKEN_HEADER_NAME) String token, @RequestBody ChangePassword changePassword) throws UserNotFoundException, InvalidPasswordException, InvalidTokenException, InvalidCredentialsException {
        userService.changePassword(token, changePassword);
    }
}
