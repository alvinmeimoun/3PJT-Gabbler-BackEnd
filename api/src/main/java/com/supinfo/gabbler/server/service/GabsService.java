package com.supinfo.gabbler.server.service;

import com.supinfo.gabbler.server.entity.Gabs;
import com.supinfo.gabbler.server.entity.User;
import com.supinfo.gabbler.server.exception.login.InvalidTokenException;
import com.supinfo.gabbler.server.exception.user.UserNotFoundException;
import com.supinfo.gabbler.server.repository.GabsRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GabsService {

    @Resource
    GabsRepository gabsRepository;

    @Resource
    UserService userService;

    public Gabs add(String token, Gabs gab) throws UserNotFoundException, InvalidTokenException {
        User loggedUser = userService.findUserForToken(token);

        gab.setUserId(loggedUser.getId());
        return gabsRepository.save(gab);
    }

}
