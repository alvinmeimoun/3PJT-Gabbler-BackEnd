package com.supinfo.gabbler.server.service;

import com.supinfo.gabbler.server.entity.Gabs;
import com.supinfo.gabbler.server.entity.Role;
import com.supinfo.gabbler.server.entity.User;
import com.supinfo.gabbler.server.exception.ResourceNotFoundException;
import com.supinfo.gabbler.server.exception.login.InvalidTokenException;
import com.supinfo.gabbler.server.exception.login.OperationNotAllowedException;
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

    public void delete(String token, Long gabsId) throws ResourceNotFoundException, OperationNotAllowedException, UserNotFoundException, InvalidTokenException {
        Gabs gab = gabsRepository.findOne(gabsId);

        if(gab == null){
            throw new ResourceNotFoundException();
        }

        User loggedUser = userService.findUserForToken(token);
        if(!gab.getUserId().equals(loggedUser.getId())){
            throw new OperationNotAllowedException();
        }

        gabsRepository.delete(gab);
    }

    public Gabs like(String token, Long gabsId) throws ResourceNotFoundException, UserNotFoundException, InvalidTokenException {
        Gabs gab = gabsRepository.findOne(gabsId);

        if(gab == null){
            throw new ResourceNotFoundException();
        }

        User loggedUser = userService.findUserForToken(token);

        gab.addLiker(loggedUser);

        return gabsRepository.save(gab);
    }

    public Gabs unlike(String token, Long gabsId) throws ResourceNotFoundException, UserNotFoundException, InvalidTokenException {
        Gabs gab = gabsRepository.findOne(gabsId);

        if(gab == null){
            throw new ResourceNotFoundException();
        }

        User loggedUser = userService.findUserForToken(token);

        gab.removeLiker(loggedUser);

        return gabsRepository.save(gab);
    }

}
