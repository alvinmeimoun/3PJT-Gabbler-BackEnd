package com.supinfo.gabbler.server.controller.api;

import com.supinfo.gabbler.server.dto.ChangePassword;
import com.supinfo.gabbler.server.dto.Subscription;
import com.supinfo.gabbler.server.exception.login.InvalidTokenException;
import com.supinfo.gabbler.server.exception.user.*;
import com.supinfo.gabbler.server.service.UserService;
import com.supinfo.gabbler.server.utils.AuthHeaderUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Alvin Meimoun
 */
@RestController
@RequestMapping(value = "/api/user")
@Api(value = "user", description = "User operations", position = 2)
public class UserController {

    @Resource
    UserService userService;

    @ApiOperation(value = "Register a new user", position = 1)
    @RequestMapping(value = "/subscribe", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public Long subscribe(@RequestBody Subscription subscription) throws UserAlreadyExistsException {
        return userService.subscribe(subscription).getId();
    }

    @ApiOperation(value = "Change password", position = 2)
    @RequestMapping(value = "/password_change", method = RequestMethod.POST, consumes = "application/json")
    public void changePassword(@RequestHeader(value = AuthHeaderUtil.TOKEN_HEADER_NAME) String token, @RequestBody ChangePassword changePassword) throws UserNotFoundException, InvalidPasswordException, InvalidTokenException, InvalidCredentialsException {
        userService.changePassword(token, changePassword);
    }

    @ApiOperation(value = "Unfollow a user", position = 4)
    @RequestMapping(value = "/unfollow", method = RequestMethod.PUT)
    public void unfollow(@RequestHeader(value = AuthHeaderUtil.TOKEN_HEADER_NAME) String token, @RequestParam(value = "userId") Long userId)
            throws UserNotFoundException, InvalidTokenException, UserAlreadyFollowedException, UserNotFollowedException {
        userService.unfollow(token, userId);
    }

    @ApiOperation(value = "Follow a user", position = 3)
    @RequestMapping(value = "/follow", method = RequestMethod.PUT)
    public void follow(@RequestHeader(value = AuthHeaderUtil.TOKEN_HEADER_NAME) String token, @RequestParam(value = "userId") Long userId)
            throws UserNotFoundException, InvalidTokenException, UserAlreadyFollowedException, UserNotFollowedException {
        userService.follow(token, userId);
    }

}
