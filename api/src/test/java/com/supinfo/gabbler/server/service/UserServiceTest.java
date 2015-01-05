package com.supinfo.gabbler.server.service;

import com.supinfo.gabbler.server.dto.Subscription;
import com.supinfo.gabbler.server.entity.Role;
import com.supinfo.gabbler.server.entity.User;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    public static final User USER_DUPLICATED = new User().setEmail("chevre.asiatiquer@gabbler.com").setNickname("chevreasiatique").setId(2L);
    public static final User USER_SHOULD_RETURN_NULL = new User().setNickname("aaanull");
    public static final Subscription USER_SUBCRIPTION_DUPLICATED = new Subscription().setEmail("chevre.asiatiquer@gabbler.com").setNickname("chevreasiatique");
    public static final Subscription USER_SUBSCRIPTION = new Subscription().setNickname("foobar").setBirthdate(new Timestamp(911082880000l))
            .setPassword("test").setEmail("foo.bar@gabbler.com");

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
}
