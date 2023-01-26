package ru.pyrinoff.chatjobparser.configuration;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.hibernate.cfg.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.pyrinoff.chatjobparser.service.PropertyService;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan("ru.pyrinoff.chatjobparser")
@EnableTransactionManagement
@EnableJpaRepositories({"ru.pyrinoff.chatjobparser.repository","ru.pyrinoff.chatjobparser.model"})
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
        factoryBean.setPackagesToScan("ru.pyrinoff.chatjobparser.model");
        @NotNull final Properties properties = new Properties();
        properties.put(Environment.DIALECT, propertyService.getDatabaseDialect());
        properties.put(Environment.HBM2DDL_AUTO, propertyService.getDatabaseHbm2ddlAuto());
        properties.put(Environment.SHOW_SQL, propertyService.getDatabaseShowSql());
        properties.put(Environment.FORMAT_SQL, propertyService.getDatabaseFormatSql());
        properties.put(Environment.USE_SECOND_LEVEL_CACHE, propertyService.getCacheUseSecondLevelCache());
        properties.put(Environment.CACHE_REGION_FACTORY, propertyService.getCacheRegionFactoryClass());
        properties.put(Environment.USE_QUERY_CACHE, propertyService.getCacheUseQueryCache());
        properties.put(Environment.USE_MINIMAL_PUTS, propertyService.getCacheUseMinimalPuts());
        properties.put(Environment.CACHE_REGION_PREFIX, propertyService.getCacheRegionPrefix());
        properties.put(Environment.CACHE_PROVIDER_CONFIG, propertyService.getCacheProviderConfigurationFile());
        factoryBean.setJpaProperties(properties);
        return factoryBean;
    }

}
