package com.supinfo.gabbler.server.config;

import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author Alvin Meimoun
 */
@Configuration
@ComponentScan(basePackages = {"com.supinfo.gabbler.server.repository", "com.supinfo.gabbler.server.entity"})
@EnableJpaRepositories("com.supinfo.gabbler.server.repository")
@EnableTransactionManagement
public class JPAConfig {

    protected static final String PROPERTY_NAME_DATABASE_URL = "db.url";

    private static final String PROPERTY_DATABASE_DRIVER = "db.driver";
    private static final String DEFAULT_DATABASE_DRIVER =  "org.h2.Driver";

    private static final String PROPERTY_DATABASE_PASSWORD =    "db.password";
    private static final String DEFAULT_DATABASE_PASSWORD =     "";

    private static final String PROPERTY_DATABASE_USERNAME =    "db.username";
    private static final String DEFAULT_DATABASE_USERNAME =     "sa";

    private static final String PROPERTY_HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String DEFAULT_HIBERNATE_DIALECT = "org.hibernate.dialect.H2Dialect";

    private static final String PROPERTY_HIBERNATE_FORMAT_SQL = "hibernate.format_sql";
    private static final String DEFAULT_HIBERNATE_FORMAT_SQL = "false";

    private static final String PROPERTY_HIBERNATE_NAMING_STRATEGY = "hibernate.ejb.naming_strategy";
    private static final String DEFAULT_HIBERNATE_NAMING_STRATEGY = "org.hibernate.cfg.ImprovedNamingStrategy";

    private static final String PROPERTY_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private static final String DEFAULT_HIBERNATE_SHOW_SQL = "false";

    private static final String PROPERTY_ENTITYMANAGER_PACKAGES_TO_SCAN = "entitymanager.packages.to.scan";
    private static final String DEFAULT_ENTITYMANAGER_PACKAGES_TO_SCAN = "com.supinfo.gabbler.server.entity";

    private static final String PROPERTY_HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";
    private static final String DEFAULT_HIBERNATE_HBM2DDL_AUTO  = "update";

    public static final String DEFAULT_EMBEDDED_DATABASE_NAME = "gabbler";

    protected static final String CONSOLE_DATABASE = "/console/database/*";

    @Resource
    Environment environment;

    @Bean
    public DataSource dataSource() {
        if (environment.containsProperty(PROPERTY_NAME_DATABASE_URL)){
            HikariDataSource dataSource = new HikariDataSource ();
            dataSource.setDriverClassName(environment.getProperty(PROPERTY_DATABASE_DRIVER, DEFAULT_DATABASE_DRIVER));
            dataSource.setJdbcUrl(environment.getProperty(PROPERTY_NAME_DATABASE_URL));
            dataSource.setUsername(environment.getProperty(PROPERTY_DATABASE_USERNAME, DEFAULT_DATABASE_USERNAME));
            dataSource.setPassword(environment.getProperty(PROPERTY_DATABASE_PASSWORD, DEFAULT_DATABASE_PASSWORD));

            return dataSource;
        } else {
            EmbeddedDatabaseBuilder embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder()
                    .setType(EmbeddedDatabaseType.H2)
                    .setName(DEFAULT_EMBEDDED_DATABASE_NAME);

            if (environment.containsProperty("db.script.initialisation"))
                embeddedDatabaseBuilder.addScript(environment.getProperty("db.script.populate"));

            return embeddedDatabaseBuilder.build();
        }
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws ClassNotFoundException  {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPackagesToScan(environment.getProperty(PROPERTY_ENTITYMANAGER_PACKAGES_TO_SCAN, DEFAULT_ENTITYMANAGER_PACKAGES_TO_SCAN));
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);

        Properties jpaProperties = new Properties();
        jpaProperties.put(PROPERTY_HIBERNATE_SHOW_SQL, environment.getProperty(PROPERTY_HIBERNATE_SHOW_SQL, DEFAULT_HIBERNATE_SHOW_SQL));
        jpaProperties.put(PROPERTY_HIBERNATE_DIALECT, environment.getProperty(PROPERTY_HIBERNATE_DIALECT, DEFAULT_HIBERNATE_DIALECT));
        jpaProperties.put(PROPERTY_HIBERNATE_HBM2DDL_AUTO, environment.getProperty(PROPERTY_HIBERNATE_HBM2DDL_AUTO, DEFAULT_HIBERNATE_HBM2DDL_AUTO));
        jpaProperties.put(PROPERTY_HIBERNATE_FORMAT_SQL, environment.getProperty(PROPERTY_HIBERNATE_FORMAT_SQL, DEFAULT_HIBERNATE_FORMAT_SQL));
        jpaProperties.put(PROPERTY_HIBERNATE_NAMING_STRATEGY, environment.getProperty(PROPERTY_HIBERNATE_NAMING_STRATEGY, DEFAULT_HIBERNATE_NAMING_STRATEGY));

        /*if (!environment.containsProperty(DatabaseConfiguration.PROPERTY_NAME_DATABASE_URL) || environment.getProperty(DatabaseConfiguration.PROPERTY_NAME_DATABASE_URL).contains("jdbc:h2") ) {
            hibernateProperties.put(PROPERTY_HIBERNATE_FORMAT_SQL, environment.getProperty(PROPERTY_HIBERNATE_FORMAT_SQL, DEFAULT_HIBERNATE_FORMAT_SQL));
            hibernateProperties.put(PROPERTY_HIBERNATE_NAMING_STRATEGY, environment.getProperty(PROPERTY_HIBERNATE_NAMING_STRATEGY, DEFAULT_HIBERNATE_NAMING_STRATEGY));
        }*/

        entityManagerFactoryBean.setJpaProperties(jpaProperties);

        return entityManagerFactoryBean;
    }

    @Bean
    public JpaTransactionManager transactionManager() throws ClassNotFoundException {
        JpaTransactionManager transactionManager =
                new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }
}
