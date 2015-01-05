package com.supinfo.gabbler.server.controller.api;

import com.supinfo.gabbler.server.dto.LoginInfo;
import com.supinfo.gabbler.server.dto.LoginResponse;
import com.supinfo.gabbler.server.exception.login.InvalidTokenException;
import com.supinfo.gabbler.server.exception.user.InvalidCredentialsException;
import com.supinfo.gabbler.server.exception.user.UserNotFoundException;
import com.supinfo.gabbler.server.service.LoginService;
import com.supinfo.gabbler.server.utils.AuthHeaderUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class LoginController {

    @Resource
    LoginService loginService;

    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    public LoginResponse login(@RequestBody LoginInfo loginInfo) throws InvalidCredentialsException, UserNotFoundException {
        return loginService.login(loginInfo);
    }

    @RequestMapping(value = "/api/logout", method = RequestMethod.GET)
    public void logout(@RequestHeader(value = AuthHeaderUtil.TOKEN_HEADER_NAME) String token) throws InvalidTokenException {
        loginService.logout(token);
    }

}
