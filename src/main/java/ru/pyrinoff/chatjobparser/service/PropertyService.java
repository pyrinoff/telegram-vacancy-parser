package ru.pyrinoff.chatjobparser.service;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Getter
@Service
@PropertySource("classpath:application.properties")
public class PropertyService {

    @Value("#{environment['database.url']}")
    private @NotNull String databaseUrl;

    @Value("#{environment['database.username']}")
    private @NotNull String databaseUsername;

    @Value("#{environment['database.password']}")
    private @NotNull String databasePassword;

    @Value("${'database.driver'}")
    private @NotNull String databaseDriver;

    @Value("#{'database.dialect'}")
    private @NotNull String databaseDialect;

    @Value("#{'database.hbm2ddl_auto'}")
    private @NotNull String databaseHbm2ddlAuto;

    @Value("#{'database.show_sql'}")
    private @NotNull String databaseShowSql;

    @Value("#{'database.format_sql'}")
    private @NotNull String databaseFormatSql;


    @NotNull
    @Value("#{environment['server.host']}")
    private String serverHost;

    @NotNull
    @Value("#{environment['server.port']}")
    private Integer serverPort;

    @NotNull
    @Value("#{environment['admin.login']}")
    private String adminLogin;

    @NotNull
    @Value("#{environment['admin.password']}")
    private String adminPassword;

    @NotNull
    @Value("#{environment['password.secret']}")
    private String passwordSecret;

    @NotNull
    @Value("#{environment['password.iteration']}")
    private Integer passwordIteration;

    @NotNull
    @Value("#{environment['session.key']}")
    private String sessionKey;

    @NotNull
    @Value("#{environment['session.timeout']}")
    private Integer sessionTimeout;

    @NotNull
    @Value("#{environment['database.cache.use_second_level_cache']}")
    private String cacheUseSecondLevelCache;

    @NotNull
    @Value("#{environment['database.cache.use_query_cache']}")
    private String cacheUseQueryCache;

    @NotNull
    @Value("#{environment['database.cache.use_minimal_puts']}")
    private String cacheUseMinimalPuts;

    @NotNull
    @Value("#{environment['database.cache.region_prefix']}")
    private String cacheRegionPrefix;

    @NotNull
    @Value("#{environment['database.cache.provider_configuration_file_resource_path']}")
    private String cacheProviderConfigurationFile;

    @NotNull
    @Value("#{environment['database.cache.region.factory_class']}")
    private String cacheRegionFactoryClass;

    @NotNull
    @Value("#{environment['database.cache.admin_protection_token']}")
    private String databaseAdminProtectionToken;

    @NotNull
    @Value("#{environment['liquibase.outputChangeLogFile']}")
    private String liquibaseChangelogFile;

}
