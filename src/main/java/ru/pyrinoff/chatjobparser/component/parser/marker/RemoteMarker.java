package ru.pyrinoff.chatjobparser.component.parser.marker;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import ru.pyrinoff.chatjobparser.model.parser.ParserServiceResult;
import ru.pyrinoff.chatjobparser.util.TextUtil;

import java.util.List;

@Component
public class RemoteMarker extends AbstractMarkerParser {

    @Override public @Nullable String getMarker(@NotNull ParserServiceResult parserServiceResult) {
        if(TextUtil.containsAny(parserServiceResult.getTextWords(), List.of("удаленка", "remote"))) return "удаленка";
        return null;
    }

}
