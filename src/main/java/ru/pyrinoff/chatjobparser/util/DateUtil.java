package ru.pyrinoff.chatjobparser.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public interface DateUtil {

    static int getDayOfMonth(@NotNull final Date date) {
        @NotNull final Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.get(Calendar.DAY_OF_MONTH);
    }

    static int getWeekOfMonth(@NotNull final Date date) {
        @NotNull final Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.get(Calendar.WEEK_OF_MONTH);
    }


    static boolean isMonday(@NotNull final Date date) {
        @NotNull final Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.get(Calendar.DAY_OF_WEEK)  == Calendar.MONDAY;
    }

}
