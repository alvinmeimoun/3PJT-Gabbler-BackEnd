package com.supinfo.gabbler.server.service;

import com.supinfo.gabbler.server.dto.LoginInfo;
import com.supinfo.gabbler.server.entity.User;
import com.supinfo.gabbler.server.exception.login.InvalidTokenException;
import com.supinfo.gabbler.server.exception.user.InvalidCredentialsException;
import com.supinfo.gabbler.server.exception.user.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceTest {

    public static final User USER_PASSWORD_PLAIN = new User().setNickname("test1").setPassword("test");
    public static final User USER_SHOULD_RETURN_NULL = new User().setNickname("testulll").setPassword("test");
    public static final String PASSWORD_TEST_CRYPTED = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";
    public static final User USER_PASSWORD_CRYPTED = new User().setNickname("test1").setPassword(PASSWORD_TEST_CRYPTED);
    public static final String TOKEN_STRING_1 = "0a12756881be6c0094e4f05b3bf9f2072d788784421bf94a9c0ed448e05ed886";
    public static final String WRONG_PASSWORD_1 = "ttes";

    @Mock
    TokenService tokenService;

    @Mock
    UserService userService;

    @InjectMocks
    LoginService loginService;

    @Before
    public void setUp() throws UserNotFoundException {
        Mockito.reset(userService);

        Mockito.when(userService.findByNickname(eq(USER_PASSWORD_PLAIN.getNickname()))).thenReturn(USER_PASSWORD_CRYPTED);
    }

    @Test
    public void should_have_injection_done() {
        assertThat(loginService.userService).isNotNull();
    }

    @Test
    public void should_check_if_credential_valid() throws UserNotFoundException {
        assertThat(loginService.isValidCredentials(USER_PASSWORD_CRYPTED.getNickname(), USER_PASSWORD_PLAIN.getPassword())).isEqualTo(true);
    }

    @Test
    public void should_check_if_credential_invalid() throws UserNotFoundException {
        assertThat(loginService.isValidCredentials(USER_PASSWORD_CRYPTED.getNickname(), WRONG_PASSWORD_1)).isEqualTo(false);
    }

    @Test
    public void should_generate_sha256_token_string() {
        assertThat(loginService.generateTokenString(USER_PASSWORD_CRYPTED.getNickname())).isNotNull().hasSize(64);
    }

    @Test
    public void should_generate_token_entity() {
        assertThat(loginService.generateTokenEntity(USER_PASSWORD_CRYPTED.getNickname(), TOKEN_STRING_1)).isNotNull();
    }

    @Test(expected = InvalidCredentialsException.class)
    public void should_not_login_and_throw_exception_if_loginInfo_is_null() throws InvalidCredentialsException, UserNotFoundException {
        loginService.login(null);
    }

    @Test(expected = InvalidCredentialsException.class)
    public void should_not_login_and_throw_exception_if_wrong_credentials() throws InvalidCredentialsException, UserNotFoundException {
        loginService.login(new LoginInfo().setUsername(USER_PASSWORD_CRYPTED.getNickname()).setPassword(WRONG_PASSWORD_1));
    }


    @Test(expected = UserNotFoundException.class)
    public void should_not_login_and_throw_user_not_found_exception() throws UserNotFoundException, InvalidCredentialsException {
        loginService.login(new LoginInfo().setUsername(USER_SHOULD_RETURN_NULL.getNickname()).setPassword(USER_SHOULD_RETURN_NULL.getPassword()));
    }

    @Test
    public void should_login_and_return_token() throws InvalidCredentialsException, UserNotFoundException {
        assertThat(loginService.login(new LoginInfo().setUsername(USER_PASSWORD_PLAIN.getNickname()).setPassword(USER_PASSWORD_PLAIN.getPassword()))).isNotNull();
    }

    @Test
    public void should_logout() throws InvalidTokenException {
        loginService.logout(TOKEN_STRING_1);
    }

    @Test(expected = InvalidTokenException.class)
    public void should_not_logout_when_token_is_null() throws InvalidTokenException {
        loginService.logout(null);
    }
}
