package com.supinfo.gabbler.server.service;

import com.supinfo.gabbler.server.entity.Role;
import com.supinfo.gabbler.server.repository.RoleRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceTest {

    public static final Role ROLE_USER = new Role().setId(1).setName("ROLE_USER");
    public static final Role ROLE_ADMIN = new Role().setId(2).setName("ROLE_ADMIN");
    public static final Role FAKE_ROLE = new Role(3, "ROLE_FAKE");
    public static final Iterable<Integer> FIND_ALL_LIST = Arrays.asList(new Integer[]{1,2});
    public static final List<Role> FIND_ALL_RETURN = Arrays.asList(new Role[]{ROLE_USER, ROLE_ADMIN});

    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    RoleService roleService;

    @Before
    public void setUp() {
        Mockito.reset(roleRepository);

        Mockito.when(roleRepository.findAll()).thenReturn(FIND_ALL_RETURN);

        Mockito.when(roleRepository.findOne(eq(ROLE_USER.getId()))).thenReturn(ROLE_USER);
        Mockito.when(roleRepository.exists(eq(ROLE_USER.getId()))).thenReturn(true);
        Mockito.when(roleRepository.findAll(eq(FIND_ALL_LIST))).thenReturn(FIND_ALL_RETURN);
        Mockito.when(roleRepository.findOne(any(Specification.class))).thenReturn(ROLE_USER);

        Mockito.when(roleRepository.save(any(Role.class))).thenReturn(ROLE_USER);
    }

    @Test
    public void should_have_injection_done() {
        assertThat(roleService.roleRepository).isNotNull();
    }

    @Test
    public void should_find_2_roles(){
        assertThat(roleService.findAll()).hasSize(2);
    }

    @Test
    public void should_find_one() {
        Assertions.assertThat(roleService.findOne(ROLE_USER.getId()).getName()).isNotNull();
    }

    @Test
    public void should_not_find_one() {
        assertThat(roleService.findOne(FAKE_ROLE.getId())).isNull();
    }

    @Test
    public void should_exists() {
        assertThat(roleService.exists(ROLE_USER.getId())).isEqualTo(true);
    }

    @Test
    public void should_not_exists() {
        assertThat(roleService.exists(FAKE_ROLE.getId())).isEqualTo(false);
    }

    @Test
    public void should_find_2_by_2_ids() {
        assertThat(roleService.findAll(FIND_ALL_LIST)).hasSize(2);
    }

    @Test
    public void should_create_user_role() {
        assertThat(roleService.findOrCreateUserRole()).isNotNull();
    }

    @Test
    public void should_find_by_name(){
        assertThat(roleService.findByName(ROLE_USER.getName())).isNotNull();
    }
}
