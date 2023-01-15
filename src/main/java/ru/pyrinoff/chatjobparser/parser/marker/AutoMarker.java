package ru.pyrinoff.chatjobparser.parser.marker;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import ru.pyrinoff.chatjobparser.model.parser.ParserServiceResult;

@Component
public class AutoMarker extends AbstractMarkerParser {

    @Override public @Nullable String getMarker(@NotNull final ParserServiceResult parserServiceResult) {
        if(parserServiceResult.getText().contains("автоматиза") || parserServiceResult.getText().contains("automation")) return "автоматизация";
        return null;
    }

}
