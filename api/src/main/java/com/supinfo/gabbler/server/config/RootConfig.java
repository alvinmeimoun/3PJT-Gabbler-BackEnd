package com.supinfo.gabbler.server.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(value = "com.supinfo.gabbler.server.config",
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = RootConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = WebMvcConfig.class),
        })
public class RootConfig {

}