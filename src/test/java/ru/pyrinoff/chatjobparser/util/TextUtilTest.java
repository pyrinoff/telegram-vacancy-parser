package ru.pyrinoff.chatjobparser.util;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TextUtilTest {

    @BeforeAll
    public static void setUp() {
    }

    @Test
    public void setUpOk() {
    }

    @Test
    public void getSetOfWords() {
        @NotNull final Set<String> stringSet = new HashSet<>() {{
            add("java");
            add("two");
            add("three");
        }} ;
        stringSet.removeAll(TextUtil.getSetOfWords("java two three"));
        Assertions.assertEquals(0, stringSet.size());
    }

}
