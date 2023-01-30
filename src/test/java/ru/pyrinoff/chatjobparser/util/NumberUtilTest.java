package ru.pyrinoff.chatjobparser.util;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
        Assertions.assertEquals(1F, NumberUtil.parseFloat(" 1 года", 3));
        Assertions.assertEquals(1.5F, NumberUtil.parseFloat(" 1.5 года", 3));
        Assertions.assertEquals(1.5F, NumberUtil.parseFloat(" 1,5 года", 3));
    }

    @Test
    public void parseFloat() {
        Assertions.assertEquals(3000F, NumberUtil.parseFloat("3000", 6));
        Assertions.assertEquals(300F, NumberUtil.parseFloat("300.0000", 6));
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
