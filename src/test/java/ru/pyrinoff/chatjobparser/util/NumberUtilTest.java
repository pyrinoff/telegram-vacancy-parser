package ru.pyrinoff.chatjobparser.util;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pyrinoff.chatjobparser.parser.salary.AbstractSalaryParser;
import ru.pyrinoff.chatjobparser.service.ParserService;

import java.util.ArrayList;
import java.util.List;

public class NumberUtilTest {

    @BeforeAll
    public static void setUp() {
    }

    @Test
    public void setUpOk() {
    }

    @Test
    public void parseDouble() {
        Assertions.assertEquals(1F, NumberUtil.parseFloat(" 1 года"));
        Assertions.assertEquals(1.5F, NumberUtil.parseFloat(" 1.5 года"));
        Assertions.assertEquals(1.5F, NumberUtil.parseFloat(" 1,5 года"));
    }

    @Test
    public void findMaxFloat() {
        @NotNull final List<Float> floatList = new ArrayList<>() {{
            add(3F);
            add(1.5F);
            add(1F);
            add(null);
        }} ;
        Assertions.assertEquals(3F, NumberUtil.findMaxFloat(floatList));
    }

}
