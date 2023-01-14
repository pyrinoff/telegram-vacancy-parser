package ru.pyrinoff.chatjobparser.util;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IntegerUtils {

    @SneakyThrows
    static @Nullable Integer parseInt(@NotNull String integerString) {
        return Integer.parseInt(integerString.replaceAll("[ .]", ""));
    }

    @SneakyThrows
    static @Nullable Float parseFloat(@NotNull String floatOrIntegerString) {
        return Float.parseFloat(floatOrIntegerString.replaceAll(" ", ""));
    }

    static boolean isValueInRange(@Nullable final Integer value, @NotNull final Integer min, @NotNull final Integer max) {
        return value >= min && value <= max;

    }

}
