package ru.pyrinoff.chatjobparser.component.parser.marker;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import ru.pyrinoff.chatjobparser.model.parser.ParserServiceResult;
import ru.pyrinoff.chatjobparser.util.TextUtil;

import java.util.List;

@Component
public class RelocationMarker extends AbstractMarkerParser {

    @Override public @Nullable String getMarker(@NotNull ParserServiceResult parserServiceResult) {
        if(
                parserServiceResult.getText().contains("релок")
                || TextUtil.containsAny(parserServiceResult.getTextWords(), List.of("relocation"))
        ) return "релокация";
        return null;
    }

}
