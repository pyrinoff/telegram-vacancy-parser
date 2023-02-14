package ru.pyrinoff.chatjobparser.component.parser.marker;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import ru.pyrinoff.chatjobparser.model.parser.ParserServiceResult;

@Component
public class ManualAutoLoadMarker extends AbstractMarkerParser {

    @Override public @Nullable String getMarker(@NotNull final ParserServiceResult parserServiceResult) {
        if(isLoad(parserServiceResult)) return "нагрузочное";

        boolean isManual = isManual(parserServiceResult);
        boolean isAuto = isAuto(parserServiceResult);

        if(isAuto && !isManual) return "авто_only";
        if(isAuto && isManual) return "авто_и_ручное";
        if(isManual) return "ручное_only";
        return "ручное_или_не_указано";
    }

    public static boolean isAuto(@NotNull final ParserServiceResult parserServiceResult) {
        return parserServiceResult.getText().contains("авто") || parserServiceResult.getText().contains("auto");
    }

    public static boolean isManual(@NotNull final ParserServiceResult parserServiceResult) {
        return parserServiceResult.getText().contains("ручн") || parserServiceResult.getTextWords().contains("manual");
    }

    public static boolean isLoad(@NotNull final ParserServiceResult parserServiceResult) {
        return parserServiceResult.getText().contains("нагруз") || parserServiceResult.getText().contains("load");
    }

}
