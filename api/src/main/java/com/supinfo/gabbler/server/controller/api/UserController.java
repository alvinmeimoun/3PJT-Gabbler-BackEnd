package com.supinfo.gabbler.server.controller.api;

import com.supinfo.gabbler.server.dto.*;
import com.supinfo.gabbler.server.entity.User;
import com.supinfo.gabbler.server.exception.file.HandledFileNotFoundException;
import com.supinfo.gabbler.server.exception.login.InvalidTokenException;
import com.supinfo.gabbler.server.exception.login.OperationNotAllowedException;
import com.supinfo.gabbler.server.exception.user.*;
import com.supinfo.gabbler.server.service.UserService;
import com.supinfo.gabbler.server.utils.AuthHeaderUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
    public Long subscribe(@RequestBody Subscription subscription) throws UserAlreadyExistsException, IOException {
        return userService.subscribe(subscription).getId();
    }

    @ApiOperation(value = "Change password", position = 2)
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/password_change", method = RequestMethod.POST, consumes = "application/json")
    public void changePassword(@RequestHeader(value = AuthHeaderUtil.TOKEN_HEADER_NAME) String token, @RequestBody ChangePassword changePassword) throws UserNotFoundException, InvalidPasswordException, InvalidTokenException, InvalidCredentialsException {
        userService.changePassword(token, changePassword);
    }

    @ApiOperation(value = "Unfollow a user", position = 4)
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/unfollow", method = RequestMethod.PUT)
    public void unfollow(@RequestHeader(value = AuthHeaderUtil.TOKEN_HEADER_NAME) String token, @RequestParam(value = "userId") Long userId)
            throws UserNotFoundException, InvalidTokenException, UserAlreadyFollowedException, UserNotFollowedException {
        userService.unfollow(token, userId);
    }

    @ApiOperation(value = "Follow a user", position = 5)
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/follow", method = RequestMethod.PUT)
    public void follow(@RequestHeader(value = AuthHeaderUtil.TOKEN_HEADER_NAME) String token, @RequestParam(value = "userId") Long userId)
            throws UserNotFoundException, InvalidTokenException, UserAlreadyFollowedException, UserNotFollowedException {
        userService.follow(token, userId);
    }

    @ApiOperation(value = "Get user informations", position = 6)
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public UserInfoDTO get(@RequestHeader(value = AuthHeaderUtil.TOKEN_HEADER_NAME) String token, @RequestParam(value = "userId") Long userId) throws UserNotFoundException, InvalidTokenException {
        return userService.getUserInfoDtoFromUserEntity(userService.getUserInformations(token, userId));
    }

    @ApiOperation(value = "Update user informations", position = 7)
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public User update(@RequestHeader(value = AuthHeaderUtil.TOKEN_HEADER_NAME) String token, @RequestBody User user) throws UserNotFoundException, InvalidTokenException, OperationNotAllowedException {
        return userService.updateUserInformations(token, user);
    }

    @ApiOperation(value = "Search user by displayName or nickname", position = 8)
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public List<UserSearchResult> update(@RequestHeader(value = AuthHeaderUtil.TOKEN_HEADER_NAME) String token, @RequestParam(value = "req") String searchRequest) {
        return userService.search(token, searchRequest);
    }

    @ApiOperation(value = "Upload profile picture", position = 9)
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/picture/profile", method = RequestMethod.POST)
    public void picture_profile_upload(@RequestHeader(value = AuthHeaderUtil.TOKEN_HEADER_NAME) String token, @RequestParam("image") MultipartFile image) throws Exception {
        userService.uploadProfilePicture(token, image);
    }

    @ApiOperation(value = "Get profile picture", position = 10)
    @RequestMapping(value = "/picture/profile", method = RequestMethod.GET)
    public void picture_profile_get(@RequestParam(value = "userID", required = true) Long userID, HttpServletResponse response) throws IOException, UserNotFoundException, HandledFileNotFoundException {
        PictureDTO dto = userService.getProfilePicture(userID);

        response.setContentType(dto.getContentType());

        InputStream is = new FileInputStream(dto.getFile());

        org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
        response.flushBuffer();
    }

    @ApiOperation(value = "Upload profile background picture", position = 11)
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/picture/profile/background", method = RequestMethod.POST)
    public void picture_profile_background_upload(@RequestHeader(value = AuthHeaderUtil.TOKEN_HEADER_NAME) String token, @RequestParam("image") MultipartFile image) throws Exception {
        userService.uploadProfileBackgroundPicture(token, image);
    }

    @ApiOperation(value = "Get profile background picture", position = 12)
    @RequestMapping(value = "/picture/profile/background", method = RequestMethod.GET)
    public void picture_profile_background_get(@RequestParam(value = "userID", required = true) Long userID, HttpServletResponse response) throws IOException, UserNotFoundException, HandledFileNotFoundException {
        PictureDTO dto = userService.getProfileBackgroundPicture(userID);

        response.setContentType(dto.getContentType());

        InputStream is = new FileInputStream(dto.getFile());

        org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
        response.flushBuffer();
    }
}
