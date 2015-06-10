package com.supinfo.gabbler.server.service;

import com.supinfo.gabbler.server.dto.ChangePassword;
import com.supinfo.gabbler.server.dto.Subscription;
import com.supinfo.gabbler.server.dto.UserInfoDTO;
import com.supinfo.gabbler.server.entity.Role;
import com.supinfo.gabbler.server.entity.Token;
import com.supinfo.gabbler.server.entity.User;
import com.supinfo.gabbler.server.exception.login.InvalidTokenException;
import com.supinfo.gabbler.server.exception.user.*;
import com.supinfo.gabbler.server.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNotNull;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    public static final User USER_NOT_FOLLOWED = new User().setNickname("imnotfollwed").setId(3l);
    public static final User USER_FOLLOWED = new User().setNickname("imfollwedyeahhh").setId(2l);
    public static final User USER_DUPLICATED = new User().setEmail("chevre.asiatiquer@gabbler.com").setNickname("chevreasiatique").setId(2L).setPassword("9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08")
            .addFollowing(USER_FOLLOWED);
    public static final User USER_SHOULD_RETURN_NULL = new User().setNickname("aaanull");
    public static final Subscription USER_SUBCRIPTION_DUPLICATED = new Subscription().setEmail("chevre.asiatiquer@gabbler.com").setNickname("chevreasiatique");
    public static final Subscription USER_SUBSCRIPTION = new Subscription().setNickname("foobar").setBirthdate(new Timestamp(911082880000l))
            .setPassword("test").setEmail("foo.bar@gabbler.com");

    //Fake tokens
    public static final String TOKEN_FOUND_STR = "testdetokennn";
    public static final String TOKEN_NOT_FOUND_STR = "testdetokennnnontrouvé";
    public static final Token TOKEN_FOUND = new Token().setLastUsed(new Date(System.currentTimeMillis())).setSeries(TOKEN_FOUND_STR).setUsername(USER_DUPLICATED.getNickname());
    public static final Token TOKEN_USER_NOT_FOUND = new Token().setLastUsed(new Date(System.currentTimeMillis())).setSeries("token_user_not_found").setUsername(USER_SHOULD_RETURN_NULL.getNickname());

    //Fake change password
    public static final ChangePassword CHANGE_PASSWORD_OK = new ChangePassword().setNewPassword("testa").setOldPassword("test");
    public static final ChangePassword CHANGE_PASSWORD_BAD_OLD = new ChangePassword().setNewPassword("testa").setOldPassword("testaa");
    public static final ChangePassword CHANGE_PASSWORD_BAD_NEW = new ChangePassword().setNewPassword("").setOldPassword("test");

    @Mock
    TokenService tokenService;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Mock RoleService roleService;

    @Before
    public void setUp() {
        Mockito.reset(userRepository);

        Mockito.when(userRepository.findByEmailOrNickname(eq(USER_SUBCRIPTION_DUPLICATED.getEmail()), eq(USER_SUBCRIPTION_DUPLICATED.getNickname()))).
                thenReturn(Arrays.asList(new User[]{USER_DUPLICATED}));
        Mockito.when(userRepository.findByNickname(eq(USER_DUPLICATED.getNickname()))).thenReturn(USER_DUPLICATED);
        Mockito.when(userRepository.findOne(any(Specification.class))).thenReturn(USER_DUPLICATED);
        Mockito.when(userRepository.findOne(eq(USER_DUPLICATED.getId()))).thenReturn(USER_DUPLICATED);
        Mockito.when(userRepository.findOne(eq(USER_FOLLOWED.getId()))).thenReturn(USER_FOLLOWED);
        Mockito.when(userRepository.findOne(eq(USER_NOT_FOLLOWED.getId()))).thenReturn(USER_NOT_FOLLOWED);

        Mockito.when(roleService.findOrCreateUserRole()).thenReturn(new Role().setId(1).setName("ROLE_USER"));

        Mockito.when(tokenService.findOne(TOKEN_FOUND_STR)).thenReturn(TOKEN_FOUND);
        Mockito.when(tokenService.findOne(TOKEN_USER_NOT_FOUND.getSeries())).thenReturn(TOKEN_USER_NOT_FOUND);

        Mockito.when(userRepository.findAll()).thenReturn(Arrays.asList(new User[]{USER_FOLLOWED, USER_DUPLICATED, USER_NOT_FOLLOWED, USER_SHOULD_RETURN_NULL}));
    }

    @Test
    public void should_have_injection_done() {
        assertThat(userService.userRepository).isNotNull();
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void should_fail_when_email_or_nickname_exist() throws UserAlreadyExistsException, IOException {
        userService.subscribe(USER_SUBCRIPTION_DUPLICATED);
    }

    @Test
    public void should_subscribe_user() throws UserAlreadyExistsException, IOException {
        User rUserId = userService.subscribe(USER_SUBSCRIPTION);
        assertThat(rUserId).isNull();
    }

    @Test
    public void should_find_by_nickname() throws UserNotFoundException {
        User rUser = userService.findByNickname(USER_DUPLICATED.getNickname());
        assertThat(rUser).isNotNull();
        assertThat(rUser.getNickname()).isEqualTo(USER_DUPLICATED.getNickname());
    }

    @Test
    public void should_find_by_email() {
        User rUser = userService.findByEmail(USER_DUPLICATED.getEmail());
        assertThat(rUser).isNotNull();
    }

    @Test
    public void should_find_user_for_token() throws UserNotFoundException, InvalidTokenException {
        assertThat(userService.findUserForToken(TOKEN_FOUND_STR));
    }

    @Test(expected = InvalidTokenException.class)
    public void should_throw_exception_when_find_user_for_token_with_bad_token() throws UserNotFoundException, InvalidTokenException {
        userService.findUserForToken(TOKEN_NOT_FOUND_STR);
    }

    @Test(expected = UserNotFoundException.class)
    public void should_throw_exception_when_find_user_for_token_with_inexistant_user() throws UserNotFoundException, InvalidTokenException {
        userService.findUserForToken(TOKEN_USER_NOT_FOUND.getSeries());
    }

    @Test
    public void should_change_password() throws UserNotFoundException, InvalidPasswordException, InvalidTokenException, InvalidCredentialsException {
        userService.changePassword(TOKEN_FOUND_STR, CHANGE_PASSWORD_OK);
        USER_DUPLICATED.setPassword("9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08");
    }

    @Test(expected = InvalidCredentialsException.class)
    public void should_not_change_password_with_bad_old_password() throws UserNotFoundException, InvalidPasswordException, InvalidTokenException, InvalidCredentialsException {
        try{
            userService.changePassword(TOKEN_FOUND_STR, CHANGE_PASSWORD_BAD_OLD);
        } catch (InvalidCredentialsException e) {
            USER_DUPLICATED.setPassword("9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08");
            throw e;
        }
    }

    @Test(expected = InvalidPasswordException.class)
    public void should_not_change_password_with_bad_new_password() throws UserNotFoundException, InvalidPasswordException, InvalidTokenException, InvalidCredentialsException {
        try{
            userService.changePassword(TOKEN_FOUND_STR, CHANGE_PASSWORD_BAD_NEW);
        } catch (InvalidPasswordException e) {
            USER_DUPLICATED.setPassword("9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08");
            throw e;
        }
    }

    @Test
    public void should_always_find_user_by_id() throws UserNotFoundException {
        assertThat(userService.findExistingUserById(USER_DUPLICATED.getId())).isNotNull();
    }

    @Test(expected = UserNotFoundException.class)
    public void should_always_find_user_by_id_but_throw_usernotfound_exception () throws UserNotFoundException {
        userService.findExistingUserById(USER_SHOULD_RETURN_NULL.getId());
    }

    @Test
    public void should_follow_user() throws UserNotFoundException, InvalidTokenException, UserAlreadyFollowedException {
        userService.follow(TOKEN_FOUND_STR, USER_NOT_FOLLOWED.getId());
        USER_DUPLICATED.removeFollowing(USER_NOT_FOLLOWED);
    }

    @Test(expected = UserAlreadyFollowedException.class)
    public void should_try_to_follow_user_already_followed_and_throw_exception() throws UserNotFoundException, InvalidTokenException, UserAlreadyFollowedException {
        userService.follow(TOKEN_FOUND_STR, USER_FOLLOWED.getId());
    }

    @Test
    public void should_unfollow_user() throws UserNotFoundException, InvalidTokenException, UserNotFollowedException, UserAlreadyFollowedException {
        userService.unfollow(TOKEN_FOUND_STR, USER_FOLLOWED.getId());
        USER_DUPLICATED.addFollowing(USER_FOLLOWED);
    }

    @Test(expected = UserNotFollowedException.class)
    public void should_try_to_unfollow_user_not_followed_and_throw_exception() throws UserNotFoundException, InvalidTokenException, UserNotFollowedException, UserAlreadyFollowedException {
        userService.unfollow(TOKEN_FOUND_STR, USER_NOT_FOLLOWED.getId());
    }

    @Test
    public void should_find_1_random_user(){
        assertThat(userService.getRecommandedUsers(null, 1)).isNotNull().hasSize(1);
    }

    @Test
    public void should_find_max_random_user(){
        assertThat(userService.getRecommandedUsers(null, 300)).isNotNull().hasSize(4);
    }
}
