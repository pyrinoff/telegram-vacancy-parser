package ru.pyrinoff.chatjobparser.unit.service;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pyrinoff.chatjobparser.unit.common.AbstractSpringTest;
import ru.pyrinoff.chatjobparser.model.parser.ParserServiceResult;
import ru.pyrinoff.chatjobparser.component.parser.salary.AbstractSalaryParser;
import ru.pyrinoff.chatjobparser.service.ParserService;

import static ru.pyrinoff.chatjobparser.service.ParserService.cleanupText;

@TestMethodOrder(MethodOrderer.MethodName.class)
public final class ParserServiceMarkerTest extends AbstractSpringTest {

    @Autowired
    private @NotNull ParserService parserService;

    @BeforeAll
    public static void setUp() {
        AbstractSalaryParser.DEBUG = true;
    }

    @Test
    public void setUpOk() {
    }


    public ParserServiceResult parseMarkersToResult(String text) {
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        Assertions.assertNotNull(parserServiceResult.getText());
        parserService.parseMarkers(parserServiceResult);;
        return parserServiceResult;
    }

    @Test
    public void parseMarkerYear1() {
        @NotNull String text = " от 1 года ";
        @NotNull final ParserServiceResult parserServiceResult = parseMarkersToResult(text);
        Assertions.assertTrue(parserServiceResult.getMarkers().contains("опыт_в_годах_1.0"));
    }

    @Test
    public void parseMarkerYear2() {
        @NotNull String text = " не менее 1,5 лет знание ";
        @NotNull final ParserServiceResult parserServiceResult = parseMarkersToResult(text);
        Assertions.assertTrue(parserServiceResult.getMarkers().contains("опыт_в_годах_1.5"));
    }

    @Test
    public void parseMarkerYear4() {
        @NotNull String text = " к требования 2 года опыта в руч ";
        @NotNull final ParserServiceResult parserServiceResult = parseMarkersToResult(text);
        Assertions.assertTrue(parserServiceResult.getMarkers().contains("опыт_в_годах_2.0"));
    }

    @Test
    public void parseMarkerYear5() {
        @NotNull String text = " и node js от года и больше! ";
        @NotNull final ParserServiceResult parserServiceResult = parseMarkersToResult(text);
        Assertions.assertTrue(parserServiceResult.getMarkers().contains("опыт_в_годах_1.0"));
    }

    @Test
    public void parseMarkerYear6() {
        @NotNull String text = "от 3-х лет из них";
        @NotNull final ParserServiceResult parserServiceResult = parseMarkersToResult(text);
        Assertions.assertTrue(parserServiceResult.getMarkers().contains("опыт_в_годах_3.0"));
    }

    @Test
    public void parseMarkerYear7() {
        @NotNull String text = "архитектуре от 3х лет;";
        @NotNull final ParserServiceResult parserServiceResult = parseMarkersToResult(text);
        Assertions.assertTrue(parserServiceResult.getMarkers().contains("опыт_в_годах_3.0"));
    }

    @Test
    public void parseMarkerYear8() {
        @NotNull String text = "ировании от 2-3 лет •Знани";
        @NotNull final ParserServiceResult parserServiceResult = parseMarkersToResult(text);
        Assertions.assertTrue(parserServiceResult.getMarkers().contains("опыт_в_годах_2.0"));
    }

    @Test
    public void parseMarkerYear9() {
        @NotNull String text = "от 11 лет";
        @NotNull final ParserServiceResult parserServiceResult = parseMarkersToResult(text);
        for(@NotNull final String oneMarker : parserServiceResult.getMarkers()) {
            Assertions.assertFalse(oneMarker.contains("опыт_в_годах"));
        }

    }

    @Test
    public void parseMarkerAuto1() {
        @NotNull String text = "ручное и автоматизированное тестирование";
        @NotNull final ParserServiceResult parserServiceResult = parseMarkersToResult(text);
        Assertions.assertEquals(parserServiceResult.getMarkers().size(), 1);
        Assertions.assertTrue(parserServiceResult.getMarkers().contains("авто_и_ручное"));
    }

    @Test
    public void parseMarkerAuto2() {
        @NotNull String text = "manual & autotesting";
        @NotNull final ParserServiceResult parserServiceResult = parseMarkersToResult(text);
        Assertions.assertEquals(parserServiceResult.getMarkers().size(), 1);
        Assertions.assertTrue(parserServiceResult.getMarkers().contains("авто_и_ручное"));
    }

    @Test
    public void parseMarkerAuto3() {
        @NotNull String text = "ручное тестирование";
        @NotNull final ParserServiceResult parserServiceResult = parseMarkersToResult(text);
        Assertions.assertEquals(parserServiceResult.getMarkers().size(), 1);
        Assertions.assertTrue(parserServiceResult.getMarkers().contains("ручное_only"));
    }

    @Test
    public void parseMarkerAuto4() {
        @NotNull String text = "manual testing";
        @NotNull final ParserServiceResult parserServiceResult = parseMarkersToResult(text);
        Assertions.assertEquals(parserServiceResult.getMarkers().size(), 1);
        Assertions.assertTrue(parserServiceResult.getMarkers().contains("ручное_only"));
    }

    @Test
    public void parseMarkerAuto5() {
        @NotNull String text = "автоматизированное тестирование";
        @NotNull final ParserServiceResult parserServiceResult = parseMarkersToResult(text);
        Assertions.assertEquals(parserServiceResult.getMarkers().size(), 1);
        Assertions.assertTrue(parserServiceResult.getMarkers().contains("авто_only"));
    }


    @Test
    public void parseMarkerAuto6() {
        @NotNull String text = "auto testing";
        @NotNull final ParserServiceResult parserServiceResult = parseMarkersToResult(text);
        Assertions.assertEquals(parserServiceResult.getMarkers().size(), 1);
        Assertions.assertTrue(parserServiceResult.getMarkers().contains("авто_only"));
    }

    @Test
    public void parseMarkerAuto7() {
        @NotNull String text = "нагрузочное тестирование и мануальное и автоматизированное";
        @NotNull final ParserServiceResult parserServiceResult = parseMarkersToResult(text);
        Assertions.assertEquals(parserServiceResult.getMarkers().size(), 1);
        Assertions.assertTrue(parserServiceResult.getMarkers().contains("нагрузочное"));
    }

    @Test
    public void parseMarkerAuto8() {
        @NotNull String text = "от 11 лет";
        @NotNull final ParserServiceResult parserServiceResult = parseMarkersToResult(text);
        Assertions.assertEquals(parserServiceResult.getMarkers().size(), 1);
        Assertions.assertTrue(parserServiceResult.getMarkers().contains("ручное_или_не_указано"));
    }


}
