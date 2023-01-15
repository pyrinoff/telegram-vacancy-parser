package ru.pyrinoff.chatjobparser.parser.word;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.pyrinoff.chatjobparser.util.TextUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class WordParser {

    public static @NotNull Set<String> parse(@NotNull final String text, @NotNull final Set<String> wordsToSearch) {
        @NotNull final Set<String> setOfWords = TextUtil.getSetOfWords(text);
        setOfWords.retainAll(wordsToSearch);
        return setOfWords;
    }

}
