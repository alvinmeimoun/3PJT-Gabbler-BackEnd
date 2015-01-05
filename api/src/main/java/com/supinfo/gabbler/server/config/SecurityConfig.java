package com.supinfo.gabbler.server.config;

import com.supinfo.gabbler.server.security.RestAccessDeniedHandler;
import com.supinfo.gabbler.server.security.RestAuthenticationEntryPoint;
import com.supinfo.gabbler.server.security.RestAuthenticationFilter;
import com.supinfo.gabbler.server.utils.EncryptionUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.filter.GenericFilterBean;

import javax.annotation.Resource;

/**
 * Spring Security Java Configuration
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackages = {"com.supinfo.gabbler.server.security"})
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;

    @Resource
    private Environment environment;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
        .and().jdbcAuthentication();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .antMatcher("/api/**")
            .addFilterBefore(authenticationFilter(), LogoutFilter.class)
                .csrf().disable().
                formLogin()
                .loginProcessingUrl("/login")
                .and().logout()
                .logoutSuccessUrl("/logout").and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authenticationEntryPoint());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        PasswordEncoder encoder = new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return EncryptionUtil.encodeToSHA256(rawPassword.toString());
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encode(rawPassword).equals(encodedPassword);
            }
        };
        return encoder;
    }

    @Bean
    public GenericFilterBean authenticationFilter() {
        return new RestAuthenticationFilter();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new RestAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

}