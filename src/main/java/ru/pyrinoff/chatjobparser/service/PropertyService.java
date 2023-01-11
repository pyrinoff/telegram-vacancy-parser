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

}
