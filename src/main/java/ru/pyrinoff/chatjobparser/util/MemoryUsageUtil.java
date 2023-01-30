package ru.pyrinoff.chatjobparser.util;

import org.jetbrains.annotations.NotNull;

public interface MemoryUsageUtil {

    static long getMemoryUsageMb() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024*1024);
    }

}
