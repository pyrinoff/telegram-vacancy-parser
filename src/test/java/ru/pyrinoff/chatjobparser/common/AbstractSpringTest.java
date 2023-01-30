package ru.pyrinoff.chatjobparser.common;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.pyrinoff.chatjobparser.configuration.ApplicationConfiguration;
import ru.pyrinoff.chatjobparser.configuration.WebMvcConfiguration;

@TestMethodOrder(MethodOrderer.MethodName.class)
//@TestPropertySource(properties = "spring.jpa.properties.hibernate.query.native=false")
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationConfiguration.class, WebMvcConfiguration.class})
public class AbstractSpringTest {

    @BeforeTestClass
    public void setup() {
    }

    @BeforeAll
    public static void setUp() {
    }

    @AfterAll
    public static void tearDown() {
//        System.out.println("Spring context closed...");
    }

}
