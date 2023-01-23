package ru.pyrinoff.chatjobparser.parser.marker;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import ru.pyrinoff.chatjobparser.model.parser.ParserServiceResult;

@Component
public abstract class AbstractMarkerParser {

    public static boolean DEBUG = false;

    abstract public @Nullable String getMarker(@NotNull final ParserServiceResult parserServiceResult);

}
