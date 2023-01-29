package ru.pyrinoff.chatjobparser.service;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pyrinoff.chatjobparser.common.AbstractSpringTest;
import ru.pyrinoff.chatjobparser.model.dto.Vacancy;
import ru.pyrinoff.chatjobparser.model.parser.ParserServiceResult;
import ru.pyrinoff.chatjobparser.parser.salary.AbstractSalaryParser;

import static ru.pyrinoff.chatjobparser.service.ParserService.cleanupText;

@TestMethodOrder(MethodOrderer.MethodName.class)
public final class ParserServiceWordTest extends AbstractSpringTest {

    @Autowired
    private @NotNull ParserService parserService;

    @BeforeAll
    public static void setUp() {
        AbstractSalaryParser.DEBUG = true;
    }

    @Test
    public void setUpOk() {
    }

    @Test
    public void parseWord1() {
        @NotNull String text = " fdsfds java fdff ffd ";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        parserService.parseWords(parserServiceResult);
        Assertions.assertArrayEquals(new String[]{"java"}, parserServiceResult.getWords().toArray());
    }

}
