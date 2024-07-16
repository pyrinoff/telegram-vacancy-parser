package ru.pyrinoff.chatjobparser.configuration;

import org.hibernate.cfg.Environment;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
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

//@ConditionalOnExpression("${database.enabled:true}")
@PropertySource("classpath:application.properties")
@Configuration
@ComponentScan(basePackages = "ru.pyrinoff.chatjobparser",
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "ru.pyrinoff.chatjobparser.controller.*")
)
@EnableJpaRepositories({
        "ru.pyrinoff.chatjobparser.repository",
        "ru.pyrinoff.chatjobparser.model"
})
@EnableTransactionManagement
public class DatabaseConfiguration {

    public final static String PACKAGES_MODEL = "ru.pyrinoff.chatjobparser.model";

    @Autowired
    private @NotNull PropertyService propertyService;
    
    @Bean
    public @NotNull DataSource dataSource() {
        System.out.println("DB URL: " + propertyService.getDatabaseUrl());
        @NotNull final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(propertyService.getDatabaseDriver());
        dataSource.setUrl(propertyService.getDatabaseUrl());
        dataSource.setUsername(propertyService.getDatabaseUsername());
        dataSource.setPassword(propertyService.getDatabasePassword());
        return dataSource;
    }

    @Bean
    public @NotNull PlatformTransactionManager transactionManager(@NotNull final LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        @NotNull final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }

    @Bean
    public @NotNull LocalContainerEntityManagerFactoryBean entityManagerFactory(@NotNull final DataSource dataSource) {
        @NotNull final LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factoryBean.setPackagesToScan(PACKAGES_MODEL);
        @NotNull final Properties properties = new Properties();
        properties.put(Environment.DIALECT, propertyService.getDatabaseDialect());
        properties.put(Environment.HBM2DDL_AUTO, propertyService.getDatabaseHbm2ddlAuto());
        properties.put(Environment.SHOW_SQL, propertyService.getDatabaseShowSql());
        properties.put(Environment.FORMAT_SQL, propertyService.getDatabaseFormatSql());
        properties.put(Environment.USE_SECOND_LEVEL_CACHE, propertyService.getCacheUseSecondLevelCache());
        if(Boolean.getBoolean(propertyService.getCacheUseSecondLevelCache())) {
            properties.put(Environment.CACHE_REGION_FACTORY, propertyService.getCacheRegionFactoryClass());
            properties.put(Environment.USE_QUERY_CACHE, propertyService.getCacheUseQueryCache());
            properties.put(Environment.USE_MINIMAL_PUTS, propertyService.getCacheUseMinimalPuts());
            properties.put(Environment.CACHE_REGION_PREFIX, propertyService.getCacheRegionPrefix());
        }
        factoryBean.setJpaProperties(properties);
        return factoryBean;
    }

}
