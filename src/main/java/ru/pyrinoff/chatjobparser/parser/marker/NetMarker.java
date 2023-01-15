package ru.pyrinoff.chatjobparser.parser.marker;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import ru.pyrinoff.chatjobparser.util.TextUtil;

import java.util.List;
import java.util.Set;

@Component
public class NetMarker extends AbstractMarkerParser {

    @Override public @Nullable String getMarker(final @NotNull String text, final @Nullable Set<String> uniqueWordsInText) {
        if(TextUtil.containsAny(uniqueWordsInText, List.of("net", "нетто", "на руки"))) return "net";
        return null;
    }

}
