package ru.pyrinoff.chatjobparser.service;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ru.pyrinoff.chatjobparser.component.parser.salary.AbstractSalaryParser;
import ru.pyrinoff.chatjobparser.model.parser.ParserServiceResult;

import javax.annotation.PostConstruct;

@Getter
@Service
@PropertySource("classpath:/application.properties")
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

    @Value("#{environment['database.native_queries_enabled']}")
    private @NotNull Boolean databaseNativeQueriesEnabled;

    @Value("#{environment['liquibase.outputChangeLogFile']}")
    private @NotNull String liquibaseChangelogFile;

    @Value("#{environment['upload.max_file_size_mb']}")
    private @NotNull Integer uploadMaxSize;

    @Value("#{environment['debug.memory']}")
    private @Nullable Boolean debugMemory;

    @Value("#{environment['similarity_percent']}")
    private @Nullable Float similarityPercent;

    //SALARY BORDERS
    @Value("#{environment['salary.borders.precise.rub.min']}")
    private @Nullable Integer salaryBordersPreciseRubMin;

    @Value("#{environment['salary.borders.precise.rub.max']}")
    private @Nullable Integer salaryBordersPreciseRubMax;

    @Value("#{environment['salary.borders.nonprecise.rub.min']}")
    private @Nullable Integer salaryBordersNonPreciseRubMin;

    @Value("#{environment['salary.borders.nonprecise.rub.max']}")
    private @Nullable Integer salaryBordersNonPreciseRubMax;

    @Value("#{environment['salary.borders.precise.usd.min']}")
    private @Nullable Integer salaryBordersPreciseUsdMin;

    @Value("#{environment['salary.borders.precise.usd.max']}")
    private @Nullable Integer salaryBordersPreciseUsdMax;

    @Value("#{environment['salary.borders.nonprecise.usd.min']}")
    private @Nullable Integer salaryBordersNonPreciseUsdMin;

    @Value("#{environment['salary.borders.nonprecise.usd.max']}")
    private @Nullable Integer salaryBordersNonPreciseUsdMax;

    @Value("#{environment['user.login']}")
    private @NotNull String userLogin;

    @Value("#{environment['user.password']}")
    private @NotNull String userPassword;

    @PostConstruct
    void setStaticVariables() {
        //думаю, это лучше, чем делать ParserServiceResult - бином (выше нагрузка), или же юзать кастомный Property-лоадер
        if(similarityPercent!=null) ParserServiceResult.SIMILARITY_PERCENT = similarityPercent;
        if(salaryBordersPreciseRubMin!=null) AbstractSalaryParser.BORDER_PRECISE_RUB_MIN = salaryBordersPreciseRubMin;
        if(salaryBordersPreciseRubMax!=null) AbstractSalaryParser.BORDER_PRECISE_RUB_MAX = salaryBordersPreciseRubMax;
        if(salaryBordersNonPreciseRubMin!=null) AbstractSalaryParser.BORDER_NON_PRECISE_RUB_MIN = salaryBordersNonPreciseRubMin;
        if(salaryBordersNonPreciseRubMax!=null) AbstractSalaryParser.BORDER_NON_PRECISE_RUB_MAX = salaryBordersNonPreciseRubMax;
        if(salaryBordersPreciseUsdMin!=null) AbstractSalaryParser.BORDER_PRECISE_USD_MIN = salaryBordersPreciseUsdMin;
        if(salaryBordersPreciseUsdMax!=null) AbstractSalaryParser.BORDER_PRECISE_USD_MAX = salaryBordersPreciseUsdMax;
        if(salaryBordersNonPreciseUsdMin!=null) AbstractSalaryParser.BORDER_NON_PRECISE_USD_MIN = salaryBordersNonPreciseUsdMin;
        if(salaryBordersNonPreciseUsdMax!=null) AbstractSalaryParser.BORDER_NON_PRECISE_USD_MAX = salaryBordersNonPreciseUsdMax;
    }

    @PostConstruct
    void runMemoryMonitor() {
        if(debugMemory!=null && debugMemory) {
            @NotNull final Thread memoryUsageThread = new Thread(new MemoryUsageThreadService());
            memoryUsageThread.start();
        }
    }

}
