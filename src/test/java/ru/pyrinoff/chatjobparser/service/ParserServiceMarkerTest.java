package ru.pyrinoff.chatjobparser.service;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pyrinoff.chatjobparser.common.AbstractSpringTest;
import ru.pyrinoff.chatjobparser.model.parser.ParserServiceResult;
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
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();        
        @NotNull String text = " от 1 года ";
        parserServiceResult.setText(cleanupText(text));
        Assertions.assertNotNull(text);
        parserService.parseMarkers(parserServiceResult);
        Assertions.assertTrue(parserServiceResult.getMarkers().contains("year1.0"));
    }

    @Test
    public void parseMarkerYear2() {
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        @NotNull String text = " не менее 1,5 лет знание ";
        parserServiceResult.setText(cleanupText(text));
        Assertions.assertNotNull(text);
        parserService.parseMarkers(parserServiceResult);
        Assertions.assertTrue(parserServiceResult.getMarkers().contains("year1.5"));
    }

    @Test
    public void parseMarkerYear3() {
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        @NotNull String text = " не менее 1,5 лет знание ";
        parserServiceResult.setText(cleanupText(text));
        Assertions.assertNotNull(text);
        parserService.parseMarkers(parserServiceResult);
        Assertions.assertTrue(parserServiceResult.getMarkers().contains("year1.5"));
    }

    @Test
    public void parseMarkerYear4() {
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        @NotNull String text = " к требования 2 года опыта в руч ";
        parserServiceResult.setText(cleanupText(text));
        Assertions.assertNotNull(text);
        parserService.parseMarkers(parserServiceResult);
        Assertions.assertTrue(parserServiceResult.getMarkers().contains("year2.0"));
    }

    @Test
    public void parseMarkerYear5() {
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        @NotNull String text = " и node js от года и больше! ";
        parserServiceResult.setText(cleanupText(text));
        Assertions.assertNotNull(text);
        parserService.parseMarkers(parserServiceResult);
        Assertions.assertTrue(parserServiceResult.getMarkers().contains("year1.0"));
    }

    @Test
    public void parseMarkerYear6() {
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        @NotNull String text = "от 3-х лет из них";
        parserServiceResult.setText(cleanupText(text));
        Assertions.assertNotNull(text);
        parserService.parseMarkers(parserServiceResult);
        Assertions.assertTrue(parserServiceResult.getMarkers().contains("year3.0"));
    }

    @Test
    public void parseMarkerYear7() {
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        @NotNull String text = "архитектуре от 3х лет;";
        parserServiceResult.setText(cleanupText(text));
        Assertions.assertNotNull(text);
        parserService.parseMarkers(parserServiceResult);
        Assertions.assertTrue(parserServiceResult.getMarkers().contains("year3.0"));
    }


    @Test
    public void parseMarkerNet() {
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        @NotNull String text = "~220-240к net годовая преми";
        parserServiceResult.setText(cleanupText(text));
        Assertions.assertNotNull(text);
        parserService.parseMarkers(parserServiceResult);
        Assertions.assertTrue(parserServiceResult.getMarkers().contains("net"));
    }

}
