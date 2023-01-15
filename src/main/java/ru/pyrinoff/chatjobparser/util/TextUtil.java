package ru.pyrinoff.chatjobparser.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public interface TextUtil {

    static String getCleanupText(@NotNull final String text) {
        return text.replaceAll("[^a-zа-я ]", "").replaceAll("\s{2,}+", " ");
    }

    static Set<String> getSetOfWords(@NotNull final String text) {
        return new HashSet<>(List.of(getCleanupText(text).split(" ")));
    }

    static boolean containsAny(final @Nullable Collection<String> haystack, final @Nullable Collection<String> needle) {
        if (haystack == null || haystack.size() == 0 || needle == null || needle.size() == 0) return false;
        return !Collections.disjoint(haystack, needle);
    }


}
