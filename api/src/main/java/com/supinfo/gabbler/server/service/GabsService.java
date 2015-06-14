package com.supinfo.gabbler.server.service;

import com.supinfo.gabbler.server.dto.GabsDTO;
import com.supinfo.gabbler.server.dto.GabsLikerDTO;
import com.supinfo.gabbler.server.entity.Gabs;
import com.supinfo.gabbler.server.entity.User;
import com.supinfo.gabbler.server.exception.ResourceNotFoundException;
import com.supinfo.gabbler.server.exception.login.InvalidTokenException;
import com.supinfo.gabbler.server.exception.login.OperationNotAllowedException;
import com.supinfo.gabbler.server.exception.user.UserNotFoundException;
import com.supinfo.gabbler.server.repository.GabsRepository;
import com.supinfo.gabbler.server.repository.specifications.GabsSpecifications;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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

    public List<GabsDTO> userTimeline(Long userId, Integer startIndex, Integer count){
        List<Gabs> gabsEntities = gabsRepository.findByUserId(userId);
        gabsEntities.sort(new Comparator<Gabs>() {
            @Override
            public int compare(Gabs o1, Gabs o2) {
                if(o1.getPostDate().getTime() > o2.getPostDate().getTime()) return -1;
                else if(o1.getPostDate().getTime() > o2.getPostDate().getTime()) return 1;
                else return 0;
            }
        });

        List<GabsDTO> gabs = new ArrayList<>();
        gabsEntities.stream().forEach(g -> {
            GabsDTO gdto = new GabsDTO();
            gdto.setContent(g.getContent());
            gdto.setUserId(g.getUserId());
            gdto.setId(g.getId());
            gdto.setPostDate(g.getPostDate());
            try {
                User fu = userService.findExistingUserById(g.getUserId());
                gdto.setDisplayName(fu.getDisplayName());
                gdto.setUserName(fu.getNickname());
            } catch (UserNotFoundException e) {
                e.printStackTrace();
                gdto.setDisplayName("Utilisateur Inconnu");
                gdto.setUserName("");
            }


            g.getLikers().stream().forEach(l -> {
                GabsLikerDTO ldto = new GabsLikerDTO();
                ldto.setUserID(l.getId());
                ldto.setDisplayName(l.getDisplayName());
                gdto.getLikers().add(ldto);
            });

            gabs.add(gdto);
        });
        gabs.sort((o1, o2) -> {
            if(o1.getPostDate().getTime() > o2.getPostDate().getTime()) return -1;
            else if(o1.getPostDate().getTime() == o2.getPostDate().getTime()) return 0;
            else return 1;
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

    public List<GabsDTO> globalTimeline(String token, Integer startIndex, Integer count) throws UserNotFoundException, InvalidTokenException {
        return globalTimeline(userService.findUserForToken(token).getId(), startIndex, count);
    }

    public List<GabsDTO> globalTimeline(Long userId, Integer startIndex, Integer count) throws UserNotFoundException {
        User loggedUser = userService.findExistingUserById(userId);

        Set<Long> followingsIDs = new HashSet<>();
        loggedUser.getFollowings().stream().forEach((user) -> followingsIDs.add(user.getId()));

        followingsIDs.add(userId);

        //Récuperation des gabs
        List<Gabs> gabsEntities = gabsRepository.findAll(GabsSpecifications.globalTimeline(followingsIDs));

        gabsEntities.sort(new Comparator<Gabs>() {
            @Override
            public int compare(Gabs o1, Gabs o2) {
                if(o1.getPostDate().getTime() > o2.getPostDate().getTime()) return -1;
                else if(o1.getPostDate().getTime() > o2.getPostDate().getTime()) return 1;
                else return 0;
            }
        });

        List<GabsDTO> gabs = new ArrayList<>();
        gabsEntities.stream().forEach(g -> {
            GabsDTO gdto = new GabsDTO();
            gdto.setContent(g.getContent());
            gdto.setId(g.getId());
            gdto.setUserId(g.getUserId());
            gdto.setPostDate(g.getPostDate());
            try {
                User fu = userService.findExistingUserById(g.getUserId());
                gdto.setDisplayName(fu.getDisplayName());
                gdto.setUserName(fu.getNickname());
            } catch (UserNotFoundException e) {
                e.printStackTrace();
                gdto.setDisplayName("Utilisateur Inconnu");
                gdto.setUserName("");
            }

            g.getLikers().stream().forEach(l -> {
                GabsLikerDTO ldto = new GabsLikerDTO();
                ldto.setUserID(l.getId());
                ldto.setDisplayName(l.getDisplayName());
                gdto.getLikers().add(ldto);
            });

            gabs.add(gdto);
        });
        gabs.sort((o1, o2) -> {
            if(o1.getPostDate().getTime() > o2.getPostDate().getTime()) return -1;
            else if(o1.getPostDate().getTime() == o2.getPostDate().getTime()) return 0;
            else return 1;
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
