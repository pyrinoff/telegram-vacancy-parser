package ru.pyrinoff.chatjobparser.component.parser.marker;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import ru.pyrinoff.chatjobparser.model.parser.ParserServiceResult;

@Component
public class LoadTestingMarker extends AbstractMarkerParser {

    @Override public @Nullable String getMarker(@NotNull ParserServiceResult parserServiceResult) {
        if(parserServiceResult.getTextWords().contains("нагрузочн")) return "нагрузочное";
        return null;
    }

}
