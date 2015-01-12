package com.supinfo.gabbler.server.service;

import com.supinfo.gabbler.server.entity.Token;
import com.supinfo.gabbler.server.repository.TokenRepository;
import com.supinfo.gabbler.server.repository.specifications.TokenSpecifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.supinfo.gabbler.server.repository.specifications.TokenSpecifications.tokenEqualUsername;

@Service
public class TokenService {

    @Resource
    TokenRepository tokenRepository;


    /*Repository*/

    @Transactional(readOnly = true)
    public Token findOne(String series){
        return tokenRepository.findOne(series);
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Token> findForUser(String username){
        return tokenRepository.findAll(tokenEqualUsername(username));
    }

    @Transactional
    public Token update(Token tokenToUpdate){
        Token token = findOne(tokenToUpdate.getSeries());
        token
                .setLastUsed(tokenToUpdate.getLastUsed())
                .setSeries(tokenToUpdate.getSeries())
                .setUsername(tokenToUpdate.getUsername());

        return save(token);
    }

    @Transactional
    public Token save(Token tokenToSave){
        return tokenRepository.save(tokenToSave);
    }

    @Transactional
    public void removeAllForUser(String username){
        List<Token> userTokens = findForUser(username);
        tokenRepository.delete(userTokens);
    }

    @Transactional
    public void removeOne(String series){
        Token token = findOne(series);
        if(token != null){
            tokenRepository.delete(token);
        }
    }
}
