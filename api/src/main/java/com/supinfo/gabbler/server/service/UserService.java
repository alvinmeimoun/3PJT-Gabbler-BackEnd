package com.supinfo.gabbler.server.service;

import com.supinfo.gabbler.server.dto.ChangePassword;
import com.supinfo.gabbler.server.dto.Subscription;
import com.supinfo.gabbler.server.entity.Token;
import com.supinfo.gabbler.server.entity.User;
import com.supinfo.gabbler.server.entity.enums.PasswordCryptMode;
import com.supinfo.gabbler.server.exception.login.InvalidTokenException;
import com.supinfo.gabbler.server.exception.user.InvalidCredentialsException;
import com.supinfo.gabbler.server.exception.user.InvalidPasswordException;
import com.supinfo.gabbler.server.exception.user.UserAlreadyExistsException;
import com.supinfo.gabbler.server.exception.user.UserNotFoundException;
import com.supinfo.gabbler.server.repository.UserRepository;
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

    /* REPOSITORY INTERFACE */
    public User findByNickname(String nickname){
        return userRepository.findByNickname(nickname);
    }

    @SuppressWarnings("unchecked")
    public User findByEmail(String email) {
        return (User) userRepository.findOne(UserSpecifications.userEqualEmail(email));
    }
}
