package ru.pyrinoff.chatjobparser.parser.marker;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import ru.pyrinoff.chatjobparser.model.parser.ParserServiceResult;
import ru.pyrinoff.chatjobparser.util.TextUtil;

import java.util.List;

@Component
public class JuniorMiddleSeniorMarker extends AbstractMarkerParser {

    @Override public @Nullable String getMarker(@NotNull final ParserServiceResult parserServiceResult) {
        if(TextUtil.containsAny(parserServiceResult.getTextWords(), List.of("senior", "seniorqa", "qasenior", "сеньора", "сеньор", "seniormiddle", "middlesenior"))) return "senior";
        if(TextUtil.containsAny(parserServiceResult.getTextWords(), List.of("middle", "middleqa", "qamiddle", "juniormiddle",  "миддл", "мидл"))) return "middle";
        if(TextUtil.containsAny(parserServiceResult.getTextWords(), List.of("junior", "джуниор"))) return "junior";
        return null;
    }

}
