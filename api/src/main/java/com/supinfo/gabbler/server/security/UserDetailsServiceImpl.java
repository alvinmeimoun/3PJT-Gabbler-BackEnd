package com.supinfo.gabbler.server.security;

import com.supinfo.gabbler.server.entity.User;
import com.supinfo.gabbler.server.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("userDetailsService")
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    UserRepository userRepository;
    @Resource
    MessageSource messageSource;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            List<User> users = userRepository.findByEmailOrNickname(username, username);
            if (users == null || users.size() == 0) {
                throw new UsernameNotFoundException("test");
                //throw new UsernameNotFoundException(messageSource.getMessage("login.exception.usernotallowed", null, I18NConfig.defaultLocal));
            }

            User user = users.get(0);
            return new org.springframework.security.core.userdetails.User(
                    username,
                    user.getPassword(),
                    user.isEnabled(),
                    user.isAccountNonExpired(),
                    user.isCredentialsNonExpired(),
                    user.isAccountNonLocked(),
                    user.getRolesAutorithies()
            );

        } catch (Exception e) {
            logger.warn("Authentification refusée pour l'utilisateur au login {}", username, e);
            throw new UsernameNotFoundException(e.getMessage());
        }
    }


}