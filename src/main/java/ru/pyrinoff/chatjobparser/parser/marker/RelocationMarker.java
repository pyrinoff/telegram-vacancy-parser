package ru.pyrinoff.chatjobparser.parser.marker;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import ru.pyrinoff.chatjobparser.util.TextUtil;

import java.util.List;
import java.util.Set;

@Component
public class RelocationMarker extends AbstractMarkerParser {

    @Override public @Nullable String getMarker(@NotNull String text, @NotNull Set<String> uniqueWordsInText) {
        if(
                text.contains("релок")
                || TextUtil.containsAny(uniqueWordsInText, List.of("relocation"))
        ) return "релокация";
        return null;
    }

}
