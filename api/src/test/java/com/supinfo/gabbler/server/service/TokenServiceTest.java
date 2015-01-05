package com.supinfo.gabbler.server.service;

import com.supinfo.gabbler.server.entity.Token;
import com.supinfo.gabbler.server.repository.TokenRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class TokenServiceTest {

    public static final Token TOKEN_USER_1 = new Token()
            .setSeries("0a12756881be6c0094e4f05b3bf9f2072d788784421bf94a9c0ed448e05ed886")
            .setUsername("user").setLastUsed(new Date(System.currentTimeMillis()));
    public static final Token TOKEN_USER_2 = new Token()
            .setSeries("0a12756881be6c0094e4f05b3bf9f2072d788784421bf94a9c0ed448e05ed222")
            .setUsername("user").setLastUsed(new Date(System.currentTimeMillis()));
    public static final Token TOKEN_FAKE = new Token().setSeries("11")
            .setLastUsed(new Date(System.currentTimeMillis())).setUsername("user");

    @Mock
    TokenRepository tokenRepository;

    @InjectMocks
    TokenService tokenService;

    @Before
    public void setUp() {
        Mockito.reset(tokenRepository);

        Mockito.when(tokenRepository.findOne(eq(TOKEN_USER_1.getSeries()))).thenReturn(TOKEN_USER_1);
        Mockito.when(tokenRepository.findAll(any(Specification.class))).thenReturn(Arrays.asList(new Token[]{TOKEN_USER_1, TOKEN_USER_2}));

        Mockito.when(tokenRepository.save(any(Token.class))).thenReturn(TOKEN_USER_1);
    }

    @Test
    public void should_have_injection_done() {
        assertThat(tokenService.tokenRepository).isNotNull();
    }

    @Test
    public void should_find_one() {
        assertThat(tokenService.findOne(TOKEN_USER_1.getSeries())).isNotNull();
    }

    @Test
    public void should_not_find_one() {
        assertThat(tokenService.findOne(TOKEN_FAKE.getSeries())).isNull();
    }

    @Test
    public void should_find_2_for_user() {
        assertThat(tokenService.findForUser(TOKEN_USER_1.getUsername())).hasSize(2);
    }

    @Test
    public void should_update_token(){
        assertThat(tokenService.update(TOKEN_USER_1)).isNotNull();
    }

    @Test
    public void should_save_token(){
        assertThat(tokenService.save(TOKEN_USER_2)).isNotNull();
    }

    @Test
    public void should_remove_all_for_user(){
        tokenService.removeAllForUser(TOKEN_USER_1.getUsername());
    }

    @Test
    public void should_remove_one(){
        tokenService.removeOne(TOKEN_USER_1.getSeries());
    }
}
