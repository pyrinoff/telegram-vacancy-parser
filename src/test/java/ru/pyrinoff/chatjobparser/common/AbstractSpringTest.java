package ru.pyrinoff.chatjobparser.common;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.pyrinoff.chatjobparser.configuration.ApplicationConfiguration;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class AbstractSpringTest {

    @NotNull
    protected final static AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext(ApplicationConfiguration.class);

    @BeforeAll
    public static void setUp() {
//        System.out.println("Spring Config initialized...");
    }

    @AfterAll
    public static void tearDown() {
//        System.out.println("Spring context closed...");
    }

}
