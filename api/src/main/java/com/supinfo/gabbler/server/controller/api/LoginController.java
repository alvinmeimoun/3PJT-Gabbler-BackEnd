package com.supinfo.gabbler.server.controller.api;

import com.supinfo.gabbler.server.dto.LoginInfo;
import com.supinfo.gabbler.server.dto.LoginResponse;
import com.supinfo.gabbler.server.exception.login.InvalidTokenException;
import com.supinfo.gabbler.server.exception.user.InvalidCredentialsException;
import com.supinfo.gabbler.server.exception.user.UserNotFoundException;
import com.supinfo.gabbler.server.service.LoginService;
import com.supinfo.gabbler.server.utils.AuthHeaderUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api(value = "login", description = "Login operations", position = 1)
public class LoginController {

    @Resource
    LoginService loginService;

    @ApiOperation(value = "Create a new session", position = 1)
    @RequestMapping(value = "/api/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public LoginResponse login(@RequestBody LoginInfo loginInfo) throws InvalidCredentialsException, UserNotFoundException, InvalidTokenException {
        return loginService.login(loginInfo);
    }

    @ApiOperation(value = "Logout current session")
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/api/logout", method = RequestMethod.GET)
    public void logout(@RequestHeader(value = AuthHeaderUtil.TOKEN_HEADER_NAME) String token) throws InvalidTokenException {
        loginService.logout(token);
    }

}
