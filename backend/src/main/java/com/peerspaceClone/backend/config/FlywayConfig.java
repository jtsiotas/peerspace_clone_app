package com.peerspaceClone.backend.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private boolean migrated = false;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof LocalContainerEntityManagerFactoryBean && !migrated) {
            System.out.println("================ RUNNING FLYWAY BEFORE JPA ================");
            DataSource dataSource = applicationContext.getBean(DataSource.class);
            Flyway.configure()
                    .dataSource(dataSource)
                    .locations("classpath:db/migration")
                    .baselineOnMigrate(true)
                    .load()
                    .migrate();
            migrated = true;
        }
        return bean;
    }
}

