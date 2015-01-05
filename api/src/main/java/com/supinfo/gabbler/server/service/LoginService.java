package com.supinfo.gabbler.server.service;

import com.supinfo.gabbler.server.dto.LoginInfo;
import com.supinfo.gabbler.server.dto.LoginResponse;
import com.supinfo.gabbler.server.entity.Token;
import com.supinfo.gabbler.server.entity.User;
import com.supinfo.gabbler.server.exception.login.InvalidTokenException;
import com.supinfo.gabbler.server.exception.user.InvalidCredentialsException;
import com.supinfo.gabbler.server.exception.user.UserNotFoundException;
import com.supinfo.gabbler.server.utils.EncryptionUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class LoginService {
    
    @Resource
    UserService userService;

    @Resource
    TokenService tokenService;

    @Transactional
    public LoginResponse login(LoginInfo loginInfo) throws InvalidCredentialsException, UserNotFoundException {
        if(loginInfo == null) throw new InvalidCredentialsException();

        boolean isValidCredentials = isValidCredentials(loginInfo.getUsername(), loginInfo.getPassword());

        if(!isValidCredentials) throw new InvalidCredentialsException();

        Token newToken = generateTokenEntity(loginInfo.getUsername(), generateTokenString(loginInfo.getUsername()));
        tokenService.save(newToken);

        return new LoginResponse().setToken(newToken.getSeries());
    }

    @Transactional(readOnly = true)
    public Boolean isValidCredentials(String nickname, String password) throws UserNotFoundException{
        User user = userService.findByNickname(nickname);

        if(user == null){
            throw new UserNotFoundException();
        }

        return user.getPassword().equals(EncryptionUtil.encodeToSHA256(password));
    }

    public String generateTokenString(String username){
        String rawToken = username + "|||3'#é" + new Date(System.currentTimeMillis()).getTime();
        return EncryptionUtil.encodeToSHA256(rawToken);
    }

    public Token generateTokenEntity(String username, String tokenString){
        Token token = new Token();
        token.setLastUsed(new Date(System.currentTimeMillis()))
                .setSeries(tokenString).setUsername(username);
        return token;
    }

    @Transactional
    public void logout(String tokenString) throws InvalidTokenException {
        if(tokenString == null) throw new InvalidTokenException();

        tokenService.removeOne(tokenString);
    }
}
