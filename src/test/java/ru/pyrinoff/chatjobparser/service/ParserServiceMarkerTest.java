package ru.pyrinoff.chatjobparser.service;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pyrinoff.chatjobparser.common.AbstractSpringTest;
import ru.pyrinoff.chatjobparser.model.dto.Vacancy;
import ru.pyrinoff.chatjobparser.parser.salary.AbstractSalaryParser;

import static ru.pyrinoff.chatjobparser.service.ParserService.cleanupText;

@TestMethodOrder(MethodOrderer.MethodName.class)
public final class ParserServiceMarkerTest extends AbstractSpringTest {

    @Autowired
    private static @NotNull ParserService parserService;

    @BeforeAll
    public static void setUp() {
        parserService = context.getBean(ParserService.class);
        AbstractSalaryParser.DEBUG = true;
    }

    @Test
    public void setUpOk() {
    }

    @Test
    public void parseMarkerYear1() {
        @NotNull final Vacancy vacancy = new Vacancy();
        @NotNull String text = " от 1 года ";
        text = cleanupText(text);
        Assertions.assertNotNull(text);
        parserService.parseMarkers(text, vacancy);
        Assertions.assertTrue(vacancy.getMarkers().contains("year1.0"));
    }

    @Test
    public void parseMarkerYear2() {
        @NotNull final Vacancy vacancy = new Vacancy();
        @NotNull String text = " не менее 1,5 лет знание ";
        text = cleanupText(text);
        Assertions.assertNotNull(text);
        parserService.parseMarkers(text, vacancy);
        Assertions.assertTrue(vacancy.getMarkers().contains("year1.5"));
    }

    @Test
    public void parseMarkerYear3() {
        @NotNull final Vacancy vacancy = new Vacancy();
        @NotNull String text = " не менее 1,5 лет знание ";
        text = cleanupText(text);
        Assertions.assertNotNull(text);
        parserService.parseMarkers(text, vacancy);
        Assertions.assertTrue(vacancy.getMarkers().contains("year1.5"));
    }

    @Test
    public void parseMarkerYear4() {
        @NotNull final Vacancy vacancy = new Vacancy();
        @NotNull String text = " к требования 2 года опыта в руч ";
        text = cleanupText(text);
        Assertions.assertNotNull(text);
        parserService.parseMarkers(text, vacancy);
        Assertions.assertTrue(vacancy.getMarkers().contains("year2.0"));
    }

    @Test
    public void parseMarkerYear5() {
        @NotNull final Vacancy vacancy = new Vacancy();
        @NotNull String text = " и node js от года и больше! ";
        text = cleanupText(text);
        Assertions.assertNotNull(text);
        parserService.parseMarkers(text, vacancy);
        Assertions.assertTrue(vacancy.getMarkers().contains("year1.0"));
    }

    @Test
    public void parseMarkerYear6() {
        @NotNull final Vacancy vacancy = new Vacancy();
        @NotNull String text = "от 3-х лет из них";
        text = cleanupText(text);
        Assertions.assertNotNull(text);
        parserService.parseMarkers(text, vacancy);
        Assertions.assertTrue(vacancy.getMarkers().contains("year3.0"));
    }

    @Test
    public void parseMarkerYear7() {
        @NotNull final Vacancy vacancy = new Vacancy();
        @NotNull String text = "архитектуре от 3х лет;";
        text = cleanupText(text);
        Assertions.assertNotNull(text);
        parserService.parseMarkers(text, vacancy);
        Assertions.assertTrue(vacancy.getMarkers().contains("year3.0"));
    }


    @Test
    public void parseMarkerNet() {
        @NotNull final Vacancy vacancy = new Vacancy();
        @NotNull String text = "~220-240к net годовая преми";
        text = cleanupText(text);
        Assertions.assertNotNull(text);
        parserService.parseMarkers(text, vacancy);
        Assertions.assertTrue(vacancy.getMarkers().contains("net"));
    }

}
