package ru.pyrinoff.chatjobparser.service;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pyrinoff.chatjobparser.common.AbstractSpringTest;

@TestMethodOrder(MethodOrderer.MethodName.class)
public final class PropertyServiceTest extends AbstractSpringTest {

    @Autowired
    private @NotNull PropertyService PROPERTY_SERVICE;

    @BeforeAll
    public static void setUp() {

    }

    @Test
    public void setupOk() {
    }

    @Test
    public void getDatabaseUrl() {
        Assertions.assertNotNull(PROPERTY_SERVICE.getDatabaseUrl());
    }

    @Test
    public void getDatabaseUsername() {
        Assertions.assertNotNull(PROPERTY_SERVICE.getDatabaseUsername());
    }

    @Test
    public void getDatabasePassword() {
        Assertions.assertNotNull(PROPERTY_SERVICE.getDatabasePassword());
    }

    @Test
    public void getDatabaseDriver() {
        Assertions.assertNotNull(PROPERTY_SERVICE.getDatabaseDriver());
    }

    @Test
    public void getDatabaseDialect() {
        Assertions.assertNotNull(PROPERTY_SERVICE.getDatabaseDialect());
    }

    @Test
    public void getDatabaseHbm2ddlAuto() {
        Assertions.assertNotNull(PROPERTY_SERVICE.getDatabaseHbm2ddlAuto());
    }

    @Test
    public void getDatabaseShowSql() {
        Assertions.assertNotNull(PROPERTY_SERVICE.getDatabaseShowSql());
    }

    @Test
    public void getDatabaseFormatSql() {
        Assertions.assertNotNull(PROPERTY_SERVICE.getDatabaseFormatSql());
    }

    @Test
    public void getCacheUseSecondLevelCache() {
        Assertions.assertNotNull(PROPERTY_SERVICE.getCacheUseSecondLevelCache());
    }

    @Test
    public void getCacheUseQueryCache() {
        Assertions.assertNotNull(PROPERTY_SERVICE.getCacheUseQueryCache());
    }

    @Test
    public void getCacheUseMinimalPuts() {
        Assertions.assertNotNull(PROPERTY_SERVICE.getCacheUseMinimalPuts());
    }

    @Test
    public void getCacheRegionPrefix() {
        Assertions.assertNotNull(PROPERTY_SERVICE.getCacheRegionPrefix());
    }

    @Test
    public void getCacheProviderConfigurationFile() {
        Assertions.assertNotNull(PROPERTY_SERVICE.getCacheProviderConfigurationFile());
    }

    @Test
    public void getCacheRegionFactoryClass() {
        Assertions.assertNotNull(PROPERTY_SERVICE.getCacheRegionFactoryClass());
    }

    @Test
    public void getCacheAdminProtectionToken() {
        Assertions.assertNotNull(PROPERTY_SERVICE.getDatabaseAdminProtectionToken());
    }

    @Test
    public void getLiquibaseChangelogFile() {
        Assertions.assertNotNull(PROPERTY_SERVICE.getLiquibaseChangelogFile());
    }

}
