package com.supinfo.gabbler.server.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.supinfo.gabbler.server.service"})
public class BeanConfig {
}
