package com.supinfo.gabbler.server.service;

import com.supinfo.gabbler.server.dto.ChangePassword;
import com.supinfo.gabbler.server.dto.Subscription;
import com.supinfo.gabbler.server.entity.Token;
import com.supinfo.gabbler.server.entity.User;
import com.supinfo.gabbler.server.entity.enums.PasswordCryptMode;
import com.supinfo.gabbler.server.exception.login.InvalidTokenException;
import com.supinfo.gabbler.server.exception.login.OperationNotAllowedException;
import com.supinfo.gabbler.server.exception.user.*;
import com.supinfo.gabbler.server.repository.UserRepository;
import com.supinfo.gabbler.server.repository.specifications.SpecificationsAndFactory;
import com.supinfo.gabbler.server.repository.specifications.UserSpecifications;
import com.supinfo.gabbler.server.utils.MathUtil;
import com.supinfo.gabbler.server.utils.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

/**
 * @author Alvin Meimoun
 */
@Service
public class UserService {

    private static int MIN_PASSWORD_LENGTH = 4;

    @Resource
    UserRepository userRepository;

    @Autowired TokenService tokenService;
    @Autowired RoleService roleService;

    @Transactional
    public User subscribe(Subscription subscription) throws UserAlreadyExistsException {
        List<User> testUserExistL = userRepository.findByEmailOrNickname(subscription.getEmail(), subscription.getNickname());

        if(testUserExistL != null && testUserExistL.size() > 0){
            throw new UserAlreadyExistsException();
        }

        //Transvasage vers un nouveau user complet avec les valeurs par défaut
        User newUser = new User().setNickname(subscription.getNickname()).setBirthdate(subscription.getBirthdate()).setFirstname(subscription.getFirstname()).
                setLastname(subscription.getLastname()).setPassword(subscription.getPassword()).setEmail(subscription.getEmail()).setDisplayName(subscription.getDisplayName())
                .setCreationDate(new Timestamp(System.currentTimeMillis())).setGender(subscription.getGender())
                .setPasswordCryptMode(PasswordCryptMode.SHA256).setActivationCode(generateActivationCode());
        if(subscription.getDisplayName() == null){
            newUser.setDisplayName(subscription.getNickname());
        }

        newUser.setPassword(EncryptionUtil.encodeToSHA256(subscription.getPassword()));

        newUser.addRoles(roleService.findOrCreateUserRole());

        return save(newUser);
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    private static String generateActivationCode(){
        int selected = MathUtil.randInt(0x1001, 0xFFFE);
        String selectedStr = Integer.toHexString(selected);
        return selectedStr.substring(0, 4).toUpperCase(Locale.ENGLISH);
    }

    @Transactional
    public void changePassword(String token, ChangePassword changePassword) throws UserNotFoundException, InvalidTokenException, InvalidCredentialsException, InvalidPasswordException {
        User user = findUserForToken(token);

        String oldCryptedPassword = EncryptionUtil.encodeToSHA256(changePassword.getOldPassword());
        
        if(!oldCryptedPassword.equals(user.getPassword())){
            throw new InvalidCredentialsException();
        }

        if(changePassword.getNewPassword() == null || changePassword.getNewPassword().length() < MIN_PASSWORD_LENGTH){
            throw new InvalidPasswordException();
        }

        String newCryptedPassword = EncryptionUtil.encodeToSHA256(changePassword.getNewPassword());
        user.setPassword(newCryptedPassword);

        userRepository.save(user);
    }

    public User findUserForToken(String token) throws InvalidTokenException, UserNotFoundException {
        Token tokenObj = tokenService.findOne(token);
        if(tokenObj == null){
            throw new InvalidTokenException();
        }

        User user = findByNickname(tokenObj.getUsername());
        if(user == null){
            throw new UserNotFoundException();
        }

        return user;
    }

    public void follow(String token, Long userId) throws UserNotFoundException, InvalidTokenException, UserAlreadyFollowedException {
        User loggedUser = findUserForToken(token);
        User toFollowUser = findExistingUserById(userId);

        if(loggedUser.getFollowings().contains(toFollowUser)){
            throw new UserAlreadyFollowedException();
        }

        loggedUser.addFollowing(toFollowUser);
        userRepository.save(loggedUser);
    }

    public void unfollow(String token, Long userId) throws UserNotFoundException, InvalidTokenException, UserAlreadyFollowedException, UserNotFollowedException {
        User loggedUser = findUserForToken(token);
        User toUnfollowUser = findExistingUserById(userId);

        if(!loggedUser.getFollowings().contains(toUnfollowUser)){
            throw new UserNotFollowedException();
        }

        loggedUser.removeFollowing(toUnfollowUser);
        userRepository.save(loggedUser);
    }

    public User getUserInformations(String token, Long userId) throws UserNotFoundException, InvalidTokenException {
        if(token != null){
            User loggedUser = findUserForToken(token);

            if(loggedUser.getId().equals(userId)) return loggedUser;
        }

        return findExistingUserById(userId);
    }

    public User updateUserInformations(String token, User user) throws UserNotFoundException, InvalidTokenException, OperationNotAllowedException {
        User loggedUser = findUserForToken(token);

        if(!loggedUser.getId().equals(user.getId())){
            throw new OperationNotAllowedException();
        }

        //Set JSON ignored and constant values to new user
        user.setPassword(loggedUser.getPassword()).setActivationCode(loggedUser.getActivationCode())
                .setPasswordCryptMode(loggedUser.getPasswordCryptMode())
                .setEnabled(loggedUser.isEnabled())
                .setAccountNonExpired(loggedUser.isAccountNonExpired())
                .setCredentialsNonExpired(loggedUser.isCredentialsNonExpired())
                .setAccountNonLocked(loggedUser.isAccountNonLocked())
                .setRoles(loggedUser.getRoles())
                .setNickname(loggedUser.getNickname());

        //update
        return userRepository.save(user);
    }

    public List<User> search(String searchRequest){
        return userRepository.findAll(UserSpecifications.userLikeDisplayNameOrNickname(searchRequest));
    }

    //END API calls

    public User findExistingUserById(Long userId) throws UserNotFoundException {
        User toFindUser = findById(userId);

        if(toFindUser == null){
            throw new UserNotFoundException();
        }

        return toFindUser;
    }

    /* REPOSITORY INTERFACE */
    public User findByNickname(String nickname){
        return userRepository.findByNickname(nickname);
    }

    @SuppressWarnings("unchecked")
    public User findByEmail(String email) {
        return (User) userRepository.findOne(UserSpecifications.userEqualEmail(email));
    }

    public User findById(Long id){
        return userRepository.findOne(id);
    }
}
