package ru.pyrinoff.chatjobparser.parser.marker;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import ru.pyrinoff.chatjobparser.model.parser.ParserServiceResult;
import ru.pyrinoff.chatjobparser.util.TextUtil;

import java.util.List;

@Component
public class NetMarker extends AbstractMarkerParser {

    @Override public @Nullable String getMarker(@NotNull ParserServiceResult parserServiceResult) {
        if(TextUtil.containsAny(parserServiceResult.getTextWords(), List.of("net", "нетто", "на руки"))) return "net";
        return null;
    }

}
