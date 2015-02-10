package com.supinfo.gabbler.server.controller.api;

import com.supinfo.gabbler.server.entity.Gabs;
import com.supinfo.gabbler.server.exception.ResourceNotFoundException;
import com.supinfo.gabbler.server.exception.login.InvalidTokenException;
import com.supinfo.gabbler.server.exception.login.OperationNotAllowedException;
import com.supinfo.gabbler.server.exception.user.UserAlreadyExistsException;
import com.supinfo.gabbler.server.exception.user.UserNotFoundException;
import com.supinfo.gabbler.server.service.GabsService;
import com.supinfo.gabbler.server.utils.AuthHeaderUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/api/gabs")
@RestController
@Api(value = "gabs", description = "Gabs operations", position = 3)
public class GabsController {

    @Resource
    GabsService gabsService;

    @ApiOperation(value = "Publish a new gabs", position = 1)
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/publish", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public Gabs publish(@RequestHeader(value = AuthHeaderUtil.TOKEN_HEADER_NAME) String token, @RequestBody Gabs gab) throws UserAlreadyExistsException, UserNotFoundException, InvalidTokenException {
        return gabsService.add(token, gab);
    }

    @ApiOperation(value = "Delete a gabs", position = 2)
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, consumes = "application/json", produces = "application/json")
    public void delete(@RequestHeader(value = AuthHeaderUtil.TOKEN_HEADER_NAME) String token, @RequestParam(value = "gabsId", required = true) Long gabsId) throws UserNotFoundException, OperationNotAllowedException, ResourceNotFoundException, InvalidTokenException {
        gabsService.delete(token, gabsId);
    }

    @ApiOperation(value = "Like a gabs", position = 3)
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/like", method = RequestMethod.PUT, produces = "application/json")
    public Gabs like(@RequestHeader(value = AuthHeaderUtil.TOKEN_HEADER_NAME) String token, @RequestParam(value = "gabsId", required = true) Long gabsId) throws UserNotFoundException, InvalidTokenException, ResourceNotFoundException {
        return gabsService.like(token, gabsId);
    }

    @ApiOperation(value = "Unlike a gabs", position = 4)
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/unlike", method = RequestMethod.PUT, produces = "application/json")
    public Gabs unlike(@RequestHeader(value = AuthHeaderUtil.TOKEN_HEADER_NAME) String token, @RequestParam(value = "gabsId", required = true) Long gabsId) throws UserNotFoundException, InvalidTokenException, ResourceNotFoundException {
        return gabsService.unlike(token, gabsId);
    }

    @ApiOperation(value = "Get all user", position = 5)
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/timeline/user", method = RequestMethod.GET, produces = "application/json")
    public List<Gabs> userTimeline(@RequestHeader(value = AuthHeaderUtil.TOKEN_HEADER_NAME) String token,
                                   @RequestParam(value = "userId") Long userId,
                                   @RequestParam(value = "startIndex", required = false, defaultValue = "0") Integer startIndex,
                                   @RequestParam(value = "count", required = false, defaultValue = "20") Integer count){
        return gabsService.userTimeline(userId, startIndex, count);
    }

}
