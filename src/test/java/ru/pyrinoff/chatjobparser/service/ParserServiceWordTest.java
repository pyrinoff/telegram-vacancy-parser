package ru.pyrinoff.chatjobparser.service;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pyrinoff.chatjobparser.common.AbstractSpringTest;
import ru.pyrinoff.chatjobparser.model.dto.Vacancy;
import ru.pyrinoff.chatjobparser.parser.salary.AbstractSalaryParser;

import static ru.pyrinoff.chatjobparser.service.ParserService.cleanupText;

@TestMethodOrder(MethodOrderer.MethodName.class)
public final class ParserServiceWordTest extends AbstractSpringTest {

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
    public void parseWord1() {
        @NotNull final Vacancy vacancy = new Vacancy();
        @NotNull String text = " fdsfds java fdff ffd ";
        text = cleanupText(text);
        Assertions.assertNotNull(text);
        parserService.parseWords(text, vacancy);
        //Assertions.assertTrue(vacancy.getMarkers().contains("java"));
        Assertions.assertArrayEquals(new String[]{"java"}, vacancy.getWords().toArray());
    }

}
