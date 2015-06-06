package com.supinfo.gabbler.server.service;

import com.supinfo.gabbler.server.dto.*;
import com.supinfo.gabbler.server.entity.Role;
import com.supinfo.gabbler.server.entity.Token;
import com.supinfo.gabbler.server.entity.User;
import com.supinfo.gabbler.server.entity.enums.PasswordCryptMode;
import com.supinfo.gabbler.server.exception.file.FileEmptyException;
import com.supinfo.gabbler.server.exception.file.HandledFileNotFoundException;
import com.supinfo.gabbler.server.exception.file.UnsupportedFormatException;
import com.supinfo.gabbler.server.exception.login.InvalidTokenException;
import com.supinfo.gabbler.server.exception.login.OperationNotAllowedException;
import com.supinfo.gabbler.server.exception.user.*;
import com.supinfo.gabbler.server.repository.UserRepository;
import com.supinfo.gabbler.server.repository.specifications.UserSpecifications;
import com.supinfo.gabbler.server.utils.EncryptionUtil;
import com.supinfo.gabbler.server.utils.FileUtil;
import com.supinfo.gabbler.server.utils.MathUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
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
    public User subscribe(Subscription subscription) throws UserAlreadyExistsException, IOException {
        List<User> testUserExistL = userRepository.findByEmailOrNickname(subscription.getEmail(), subscription.getNickname());
        User returnUser = null;

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

        Role userRole = roleService.findOrCreateUserRole();
        newUser.addRoles(userRole);

        //Récupération de la photo de profil depuis gravatar
        try{
            String gravatarUrlStr = String.format("http://www.gravatar.com/avatar/%s.jpg?d=mm", DigestUtils.md5Hex(subscription.getEmail()));
            URL gravatarUrl = new URL(gravatarUrlStr);

            File profileImageFile = new File(FileUtils.getUserDirectoryPath() + String.format("/Gabbler/picture/profile/%s.%s", newUser.getNickname(),
                    FileUtil.getFileExtensionFromMimetype("image/jpeg")));
            if(!profileImageFile.exists()) {
                new File(FileUtils.getUserDirectoryPath() + "/Gabbler/picture/profile/").mkdirs();
                profileImageFile.createNewFile();
            }

            ReadableByteChannel rbc = Channels.newChannel(gravatarUrl.openStream());
            FileOutputStream fosImageProfile = new FileOutputStream(profileImageFile);
            fosImageProfile.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            newUser.setProfilePictureMimetype("image/jpeg");
        } catch (Exception ex){
            System.out.println("Erreur lors de l'obtention du gravatar : ");
            ex.printStackTrace();
        }

        returnUser = save(newUser);
        return returnUser;
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

    public List<UserSearchResult> search(String token, String searchRequest){
        List<User> foundEntities = userRepository.findAll(UserSpecifications.userLikeDisplayNameOrNickname(searchRequest));
        List<UserSearchResult> results = new ArrayList<>();

        User currentUser = null;
        if(token != null){
            try{
                currentUser = findUserForToken(token);
            } catch (InvalidTokenException|UserNotFoundException e){

            }
        }

        final User currentUserRef = currentUser;


        foundEntities.forEach(e -> { UserSearchResult r  = new UserSearchResult().setBackgroundPictureMimetype(e.getBackgroundPictureMimetype())
            .setBirthdate(e.getBirthdate()).setCreationDate(e.getCreationDate()).setDisplayName(e.getDisplayName()).setEmail(e.getEmail())
            .setFirstname(e.getFirstname()).setGender(e.getGender()).setId(e.getId()).setLastname(e.getLastname())
            .setNickname(e.getNickname()).setPhone(e.getPhone()).setPhoneIndicator(e.getPhoneIndicator())
            .setProfilePictureMimetype(e.getProfilePictureMimetype());

            if(currentUserRef == null) r.setIsFollowing(false);
            else {
                r.setIsFollowing(currentUserRef.getFollowings().contains(e));
            }

            results.add(r);
        });

        return results;
    }

    public void uploadProfilePicture(String token, MultipartFile file) throws Exception{
        String contentType = file.getContentType();
        if(!contentType.equals("image/png") && !contentType.equals("image/jpeg")){
            throw new UnsupportedFormatException();
        }

        User user = findUserForToken(token);
        byte[] imageBytes = getByteArrayFromMultipartFile(file);
        FileUtils.writeByteArrayToFile(new File(FileUtils.getUserDirectoryPath() + String.format("/Gabbler/picture/profile/%s.%s", user.getNickname(),
                FileUtil.getFileExtensionFromMimetype(contentType))), imageBytes);

        user.setProfilePictureMimetype(contentType);
        userRepository.save(user);
    }

    public PictureDTO getProfilePicture(Long userID) throws UserNotFoundException, HandledFileNotFoundException {
        User user = findExistingUserById(userID);

        if(user.getProfilePictureMimetype() == null) throw new HandledFileNotFoundException();

        String contentType = user.getProfilePictureMimetype();

        return new PictureDTO().setFile(new File(FileUtils.getUserDirectoryPath() + String.format("/Gabbler/picture/profile/%s.%s", user.getNickname(),
                FileUtil.getFileExtensionFromMimetype(contentType)))).setContentType(contentType);
    }

    public void uploadProfileBackgroundPicture(String token, MultipartFile file) throws Exception{
        String contentType = file.getContentType();
        if(!contentType.equals("image/png") && !contentType.equals("image/jpeg")){
            throw new UnsupportedFormatException();
        }

        User user = findUserForToken(token);
        byte[] imageBytes = getByteArrayFromMultipartFile(file);
        FileUtils.writeByteArrayToFile(new File(FileUtils.getUserDirectoryPath() + String.format("/Gabbler/picture/profile/background/%d.%s", user.getId(),
                FileUtil.getFileExtensionFromMimetype(contentType))), imageBytes);

        user.setBackgroundPictureMimetype(contentType);
        userRepository.save(user);
    }

    public PictureDTO getProfileBackgroundPicture(Long userID) throws UserNotFoundException, HandledFileNotFoundException {
        User user = findExistingUserById(userID);

        if(user.getBackgroundPictureMimetype() == null) throw new HandledFileNotFoundException();

        String contentType = user.getBackgroundPictureMimetype();

        return new PictureDTO().setFile(new File(FileUtils.getUserDirectoryPath() + String.format("/Gabbler/picture/profile/background/%d.%s", user.getId(),
                FileUtil.getFileExtensionFromMimetype(contentType)))).setContentType(contentType);
    }

    //END API calls

    public byte[] getByteArrayFromMultipartFile(MultipartFile file) throws Exception{
        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            return bytes;
        } else {
            throw new FileEmptyException();
        }
    }

    public User findExistingUserById(Long userId) throws UserNotFoundException {
        User toFindUser = findById(userId);

        if(toFindUser == null){
            throw new UserNotFoundException();
        }

        return toFindUser;
    }

    public UserInfoDTO getUserInfoDtoFromUserEntity(User entity){
        return new UserInfoDTO()
                .setBackgroundPictureMimetype(entity.getBackgroundPictureMimetype())
                .setBirthdate(entity.getBirthdate())
                .setCreationDate(entity.getCreationDate())
                .setDisplayName(entity.getDisplayName())
                .setEmail(entity.getEmail())
                .setEmailValidated(entity.isEmailValidated())
                .setFirstname(entity.getFirstname())
                .setGender(entity.getGender())
                .setId(entity.getId())
                .setLastname(entity.getLastname())
                .setNbFollowers(entity.getFollowers().size())
                .setNbFollowings(entity.getFollowings().size())
                .setNickname(entity.getNickname())
                .setPhone(entity.getPhone())
                .setPhoneIndicator(entity.getPhoneIndicator())
                .setProfilePictureMimetype(entity.getProfilePictureMimetype());
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
