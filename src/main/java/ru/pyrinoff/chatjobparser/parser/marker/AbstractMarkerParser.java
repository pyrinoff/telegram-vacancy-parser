package ru.pyrinoff.chatjobparser.parser.marker;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractMarkerParser {

    public static volatile boolean DEBUG = false;

    abstract public @Nullable String getMarker(@NotNull String text);

}
