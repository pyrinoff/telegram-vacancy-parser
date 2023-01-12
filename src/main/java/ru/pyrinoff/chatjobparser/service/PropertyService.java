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

    @Value("#{environment['database.driver']}")
    private @NotNull String databaseDriver;

    @Value("#{environment['database.dialect']}")
    private @NotNull String databaseDialect;

    @Value("#{environment['database.hbm2ddl_auto']}")
    private @NotNull String databaseHbm2ddlAuto;

    @Value("#{environment['database.show_sql']}")
    private @NotNull String databaseShowSql;

    @Value("#{environment['database.format_sql']}")
    private @NotNull String databaseFormatSql;

    @Value("#{environment['database.cache.use_second_level_cache']}")
    private @NotNull String cacheUseSecondLevelCache;

    @Value("#{environment['database.cache.use_query_cache']}")
    private @NotNull String cacheUseQueryCache;

    @Value("#{environment['database.cache.use_minimal_puts']}")
    private @NotNull String cacheUseMinimalPuts;

    @Value("#{environment['database.cache.region_prefix']}")
    private @NotNull String cacheRegionPrefix;

    @Value("#{environment['database.cache.provider_configuration_file_resource_path']}")
    private @NotNull String cacheProviderConfigurationFile;

    @Value("#{environment['database.cache.region.factory_class']}")
    private @NotNull String cacheRegionFactoryClass;

    @Value("#{environment['database.admin_protection_token']}")
    private @NotNull String databaseAdminProtectionToken;

    @Value("#{environment['liquibase.outputChangeLogFile']}")
    private @NotNull String liquibaseChangelogFile;

}
