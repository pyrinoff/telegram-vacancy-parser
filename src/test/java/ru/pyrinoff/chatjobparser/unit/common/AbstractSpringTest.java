package ru.pyrinoff.chatjobparser.unit.common;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@TestMethodOrder(MethodOrderer.MethodName.class)
//@TestPropertySource(properties = "spring.jpa.properties.hibernate.query.native=false")
//@ExtendWith(SpringExtension.class)
//@WebAppConfiguration
//@ContextConfiguration(classes = {SpringApplicationConfiguraion.class})
@SpringBootTest
@ExtendWith(SpringExtension.class)
//@AutoConfigureMockMvc
public class AbstractSpringTest {

    @BeforeTestClass
    public void setup() {
    }

    @BeforeAll
    public static void setUp() {
    }

    @AfterAll
    public static void tearDown() {
    }

}
