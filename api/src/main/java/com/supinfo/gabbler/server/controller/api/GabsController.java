package com.supinfo.gabbler.server.controller.api;

import com.supinfo.gabbler.server.entity.Gabs;
import com.supinfo.gabbler.server.exception.login.InvalidTokenException;
import com.supinfo.gabbler.server.exception.user.UserAlreadyExistsException;
import com.supinfo.gabbler.server.exception.user.UserNotFoundException;
import com.supinfo.gabbler.server.service.GabsService;
import com.supinfo.gabbler.server.utils.AuthHeaderUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RequestMapping("/api/gabs")
@RestController
@Api(value = "gabs", description = "Gabs operations", position = 3)
public class GabsController {

    @Resource
    GabsService gabsService;

    @ApiOperation(value = "Publish a new gabs", position = 1)
    @RequestMapping(value = "/publish", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public Gabs subscribe(@RequestHeader(value = AuthHeaderUtil.TOKEN_HEADER_NAME) String token, @RequestBody Gabs gab) throws UserAlreadyExistsException, UserNotFoundException, InvalidTokenException {
        return gabsService.add(token, gab);
    }

}
