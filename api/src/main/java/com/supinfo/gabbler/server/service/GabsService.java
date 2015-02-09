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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    public List<Gabs> userTimeline(Long userId, Integer startIndex, Integer count){
        List<Gabs> gabs = gabsRepository.findByUserId(userId);
        gabs.sort(new Comparator<Gabs>() {
            @Override
            public int compare(Gabs o1, Gabs o2) {
                if(o1.getPostDate().getTime() > o2.getPostDate().getTime()) return -1;
                else if(o1.getPostDate().getTime() > o2.getPostDate().getTime()) return 1;
                else return 0;
            }
        });

        try{
            return gabs.subList(startIndex, startIndex+count);
        } catch (IndexOutOfBoundsException e){
            try{
                return gabs.subList(startIndex, gabs.size());
            } catch (IndexOutOfBoundsException e1){
                return new ArrayList<>();
            }
        }

    }

}
