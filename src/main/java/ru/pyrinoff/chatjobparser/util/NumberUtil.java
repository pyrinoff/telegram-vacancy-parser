package ru.pyrinoff.chatjobparser.util;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface NumberUtil {

    @SneakyThrows
    static @Nullable Float parseFloat(@NotNull String floatOrIntegerString, int maxNumbersWithPointer) {
        @NotNull String partWithNumbers = floatOrIntegerString.replaceAll(",", ".");
        partWithNumbers = partWithNumbers.replaceAll(" ", "");

        Map<Integer, List<String>> matches = RegexUtils.getMatches(partWithNumbers, "^[0-9.]{1,"+maxNumbersWithPointer+"}");
        if (matches == null || matches.get(0)==null || matches.get(0).get(0)==null) return null;
        @NotNull String finalStringOfNumber = matches.get(0).get(0);

        try {
            return Float.parseFloat(finalStringOfNumber);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    static @Nullable Float findMaxFloat(@Nullable final List<Float> list)
    {
        @Nullable final List<Float> filteredList = list.stream().filter(oneFloat -> oneFloat != null).collect(Collectors.toList());
        if (filteredList == null || filteredList.size() == 0)  return null;
        Collections.sort(filteredList);
        return filteredList.get(filteredList.size() - 1);
    }

    static boolean isValueInRange(@Nullable final Integer value, @NotNull final Integer min, @NotNull final Integer max) {
        return value >= min && value <= max;

    }

}
