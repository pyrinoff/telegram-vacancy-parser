package ru.pyrinoff.chatjobparser.configuration;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.hibernate.cfg.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import ru.pyrinoff.chatjobparser.service.PropertyService;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan("ru.pyrinoff.jobparser")
public class ApplicationConfiguration {

    @Autowired
    private @NotNull PropertyService propertyService;

    @Bean
    @NotNull
    public DataSource dataSource() {
        @NotNull final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(propertyService.getDatabaseDriver());
        dataSource.setUrl(propertyService.getDatabaseUrl());
        dataSource.setUsername(propertyService.getDatabaseUsername());
        dataSource.setPassword(propertyService.getDatabasePassword());
        return dataSource;
    }

    @Bean
    @NotNull
    public PlatformTransactionManager transactionManager(@NotNull final LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        @NotNull final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }

    @Bean
    @NotNull
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@NotNull final DataSource dataSource) {
        @NotNull final LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factoryBean.setPackagesToScan("ru.pyrinoff.jobparser.model");
        @NotNull final Properties properties = new Properties();
        properties.put(Environment.DIALECT, propertyService.getDatabaseDialect());
        properties.put(Environment.HBM2DDL_AUTO, propertyService.getDatabaseHbm2ddlAuto());
        properties.put(Environment.SHOW_SQL, propertyService.getDatabaseShowSql());
//        properties.put(Environment.FORMAT_SQL, propertyService.getDBFormatSql());
//        properties.put(Environment.USE_SECOND_LEVEL_CACHE, propertyService.getDBSecondLvlCache());
//        properties.put(Environment.CACHE_REGION_FACTORY, propertyService.getDBFactoryClass());
//        properties.put(Environment.USE_QUERY_CACHE, propertyService.getDBUseQueryCache());
//        properties.put(Environment.USE_MINIMAL_PUTS, propertyService.getDBUseMinPuts());
//        properties.put(Environment.CACHE_REGION_PREFIX, propertyService.getDBRegionPrefix());
//        properties.put(Environment.CACHE_PROVIDER_CONFIG, propertyService.getDBConfigFilePath());
        factoryBean.setJpaProperties(properties);
        return factoryBean;
    }

}
