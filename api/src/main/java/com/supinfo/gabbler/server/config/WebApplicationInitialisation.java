package com.supinfo.gabbler.server.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Class permettant d'initier la configuration de globale de l'application.
 * Cette classe se substitue au Web.xml.
 */

public class WebApplicationInitialisation extends AbstractAnnotationConfigDispatcherServletInitializer {

    //private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /*@Resource
    private Environment environment;*/

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{RootConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebMvcConfig.class, SecurityConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }


    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        /*logger.info("Mise en place de la console H2 à l'url {}", DatabaseConfig.CONSOLE_DATABASE);
        ServletRegistration.Dynamic h2Servlet = servletContext.addServlet("h2console", WebServlet.class);
        h2Servlet.setLoadOnStartup(2);
        h2Servlet.addMapping(JPAConfig.CONSOLE_DATABASE);*/

        super.onStartup(servletContext);
    }
}