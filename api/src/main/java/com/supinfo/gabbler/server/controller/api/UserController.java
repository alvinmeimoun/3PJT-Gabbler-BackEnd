package com.supinfo.gabbler.server.controller.api;

import com.supinfo.gabbler.server.dto.Subscription;
import com.supinfo.gabbler.server.exception.user.UserAlreadyExistsException;
import com.supinfo.gabbler.server.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

}
