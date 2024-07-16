package ru.pyrinoff.chatjobparser.unit.service;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.pyrinoff.chatjobparser.service.PropertyService;
import ru.pyrinoff.chatjobparser.unit.common.AbstractSpringTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@TestMethodOrder(MethodOrderer.MethodName.class)
//@PropertySource("classpath:/application-test.properties")
@ActiveProfiles("test")
public final class PropertyServiceTest extends AbstractSpringTest {

    @Autowired
    private PropertyService propertyService;

    @Test
    public void testDatabaseProperties() {
        assertEquals("jdbc:hsqldb:mem:tm", propertyService.getDatabaseUrl());
        assertEquals("sa", propertyService.getDatabaseUsername());
        assertEquals("", propertyService.getDatabasePassword());
        assertEquals("org.hsqldb.jdbcDriver", propertyService.getDatabaseDriver());
        assertEquals("org.hibernate.dialect.HSQLDialect", propertyService.getDatabaseDialect());
        assertEquals("create", propertyService.getDatabaseHbm2ddlAuto());
        assertEquals("true", propertyService.getDatabaseShowSql());
        assertEquals("true", propertyService.getDatabaseFormatSql());
        assertEquals("false", propertyService.getCacheUseSecondLevelCache());
        assertEquals("false", propertyService.getCacheUseQueryCache());
        assertEquals("false", propertyService.getCacheUseMinimalPuts());
        assertEquals("jp", propertyService.getCacheRegionPrefix());
        assertEquals("hazelcast.xml", propertyService.getCacheProviderConfigurationFile());
        assertEquals("com.hazelcast.hibernate.HazelcastCacheRegionFactory", propertyService.getCacheRegionFactoryClass());
        assertEquals("someToken", propertyService.getDatabaseAdminProtectionToken());
        assertFalse(propertyService.getDatabaseNativeQueriesEnabled());
    }

    @Test
    public void testLiquibaseProperties() {
        assertEquals("src/main/resources/changelog/changelog-master.xml", propertyService.getLiquibaseChangelogFile());
    }

    @Test
    public void testDebugProperties() {
        assertFalse(propertyService.getDebugMemory());
    }

    @Test
    public void testSimilarityProperties() {
        assertEquals(95.0f, propertyService.getSimilarityPercent());
    }

    @Test
    public void testSalaryBordersProperties() {
        assertEquals(15000, propertyService.getSalaryBordersPreciseRubMin());
        assertEquals(800000, propertyService.getSalaryBordersPreciseRubMax());
        assertEquals(15000, propertyService.getSalaryBordersNonPreciseRubMin());
        assertEquals(450000, propertyService.getSalaryBordersNonPreciseRubMax());
        assertEquals(300, propertyService.getSalaryBordersPreciseUsdMin());
        assertEquals(12000, propertyService.getSalaryBordersPreciseUsdMax());
        assertEquals(1000, propertyService.getSalaryBordersNonPreciseUsdMin());
        assertEquals(7000, propertyService.getSalaryBordersNonPreciseUsdMax());
    }

    @Test
    public void testDefaultUserProperties() {
        assertEquals("admin", propertyService.getUserLogin());
        assertEquals("admin", propertyService.getUserPassword());
    }

}
