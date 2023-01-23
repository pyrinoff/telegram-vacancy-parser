package ru.pyrinoff.chatjobparser.parser.word;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.pyrinoff.chatjobparser.model.parser.ParserServiceResult;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class WordParser {

    public static @NotNull Set<String> parse(@NotNull final ParserServiceResult parserServiceResult, @Nullable final Set<String> wordsToSearch) {
        if(wordsToSearch==null) return Collections.emptySet();
        @NotNull final Set<String> setOfWords = new HashSet<>(parserServiceResult.getTextWords());
        setOfWords.retainAll(wordsToSearch);
        return setOfWords;
    }

}
