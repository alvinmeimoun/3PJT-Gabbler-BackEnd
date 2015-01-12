package com.supinfo.gabbler.server.controller.api;

import com.mangofactory.swagger.annotations.ApiIgnore;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/hello")
@ApiIgnore
public class HelloRestController {

    @RequestMapping(value = "/say", method = RequestMethod.GET)
    public String sayHello() {
        return "hello rest";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/say/user", method = RequestMethod.GET)
    public String sayHelloWithUserProtection(){
        return "Hello rest USER";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/say/admin", method = RequestMethod.GET)
    public String sayHelloWithAdminProtection(){
        return "Hello rest ADMIN";
    }
}
