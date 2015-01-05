package com.supinfo.gabbler.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.MultipartConfigElement;
import java.util.List;

@EnableWebMvc
@ComponentScan("com.supinfo.gabbler.server.controller")
@EnableTransactionManagement
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    public static final int CACHE_PERIOD = 31556926;
    public static final String JSP_PREFIX = "/WEB-INF/pages/";
    public static final String JSP_SUFFIX = ".jsp";

    public static String VERSION = "version";
    private List<HttpMessageConverter<?>> messageConverters; // Cached: this is not a bean.
    /*private @Resource
    Environment environment;
    private @Resource
    LocaleChangeInterceptor localeChangeInterceptor;*/


    /**
     * Enregistrement standards des Controllers
     *
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        super.addViewControllers(registry);
    }

    /**
     * Déclaration des ressources statics, par défaut la racine de la webapp est utilisé.
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //super.addResourceHandlers(registry);                                                                                                                                           ,+

        registry.addResourceHandler("/css/**").addResourceLocations("/static/css/").setCachePeriod(CACHE_PERIOD);
        registry.addResourceHandler("/img/**").addResourceLocations("/static/img/").setCachePeriod(CACHE_PERIOD);
        registry.addResourceHandler("/js/**").addResourceLocations("/static/js/").setCachePeriod(CACHE_PERIOD);
        registry.addResourceHandler("/fonts/**").addResourceLocations("/static/fonts/").setCachePeriod(CACHE_PERIOD);
        registry.addResourceHandler("/partial/**").addResourceLocations("/static/partial/").setCachePeriod(CACHE_PERIOD);
        registry.addResourceHandler("/less/**").addResourceLocations("/static/less/").setCachePeriod(CACHE_PERIOD);
    }

    /**
     * Configuration du InternalResourceViewResolver associé à la localisation des JSPs.
     *
     * @return
     */
    @Bean
    public InternalResourceViewResolver getInternalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix(JSP_PREFIX);
        resolver.setSuffix(JSP_SUFFIX);
        return resolver;
    }

    /**
     * Défini le <mvc:default-servlet-handler/>.
     *
     * @param configurer
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void configureMessageConverters (List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        converters.add(new ResourceHttpMessageConverter());
        converters.add(mappingJackson2HttpMessageConverter);
        converters.add(new StringHttpMessageConverter());
    }

    /*@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor);
    }*/

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        return new MultipartConfigElement("");
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }
}