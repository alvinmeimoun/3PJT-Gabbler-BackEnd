package com.supinfo.gabbler.server.service;

import com.supinfo.gabbler.server.dto.ChangePassword;
import com.supinfo.gabbler.server.dto.Subscription;
import com.supinfo.gabbler.server.entity.Role;
import com.supinfo.gabbler.server.entity.Token;
import com.supinfo.gabbler.server.entity.User;
import com.supinfo.gabbler.server.exception.login.InvalidTokenException;
import com.supinfo.gabbler.server.exception.user.InvalidCredentialsException;
import com.supinfo.gabbler.server.exception.user.InvalidPasswordException;
import com.supinfo.gabbler.server.exception.user.UserAlreadyExistsException;
import com.supinfo.gabbler.server.exception.user.UserNotFoundException;
import com.supinfo.gabbler.server.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    public static final User USER_DUPLICATED = new User().setEmail("chevre.asiatiquer@gabbler.com").setNickname("chevreasiatique").setId(2L).setPassword("9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08");
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

        Mockito.when(roleService.findOrCreateUserRole()).thenReturn(new Role().setId(1).setName("ROLE_USER"));

        Mockito.when(tokenService.findOne(TOKEN_FOUND_STR)).thenReturn(TOKEN_FOUND);
        Mockito.when(tokenService.findOne(TOKEN_USER_NOT_FOUND.getSeries())).thenReturn(TOKEN_USER_NOT_FOUND);
    }

    @Test
    public void should_have_injection_done() {
        assertThat(userService.userRepository).isNotNull();
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void should_fail_when_email_or_nickname_exist() throws UserAlreadyExistsException {
        userService.subscribe(USER_SUBCRIPTION_DUPLICATED);
    }

    @Test
    public void should_subscribe_user() throws UserAlreadyExistsException{
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
}
