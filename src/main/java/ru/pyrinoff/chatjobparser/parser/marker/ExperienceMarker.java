package ru.pyrinoff.chatjobparser.parser.marker;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import ru.pyrinoff.chatjobparser.model.parser.ParserServiceResult;
import ru.pyrinoff.chatjobparser.util.NumberUtil;
import ru.pyrinoff.chatjobparser.util.RegexUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ExperienceMarker extends AbstractMarkerParser {

    private static final @NotNull String pattern1 = "(от|не менее)\\s?([1-9]?\\s{0,1}года|[1-9,.-х-]{1,3}\\s?лет)";
    private static final @NotNull String pattern2 = "([1-9,.]{1,3}) (года|год|лет) (опыта|в сфере)";

    private static final @NotNull Float MAX_YEAR = 10F;

    public static @Nullable Float patternOneMaxValue(@NotNull String text) {
        @NotNull final Map<Integer, List<String>> matches = RegexUtils.getMatches(text, pattern1);
        if (matches == null) return null;

        @NotNull final List<Float> numbers = new ArrayList<>();

        for (int i = 0; i < matches.size(); i++) {
            @NotNull final String stringWithNumber = matches.get(i).get(2);
            @Nullable final Float floatValue = NumberUtil.parseFloat(stringWithNumber, 3);
            if (floatValue == null) {
                if (stringWithNumber.contains("год")) numbers.add(1F);
                continue;
            }
            numbers.add(floatValue);
        }
        if (numbers.size() < 1) return null;
        @NotNull final Float year = NumberUtil.findMaxFloat(numbers);
        if(!isLooksNormal(year)) return null;
        return year;
    }

    public static @Nullable Float patternTwoMaxValue(@NotNull String text) {
        @NotNull final Map<Integer, List<String>> matches = RegexUtils.getMatches(text, pattern2);
        if (matches == null) return null;

        @NotNull final List<Float> numbers = new ArrayList<>();

        for (int i = 0; i < matches.size(); i++) {
            @NotNull final String stringWithNumber = matches.get(i).get(1);
            @Nullable final Float floatValue = NumberUtil.parseFloat(stringWithNumber, 3);
            numbers.add(floatValue);
        }
        if (numbers.size() < 1) return null;
        @NotNull final Float year = NumberUtil.findMaxFloat(numbers);
        if(!isLooksNormal(year)) return null;
        return year;
    }

    @Override public @Nullable String getMarker(@NotNull final ParserServiceResult parserServiceResult) {
        @NotNull final List<Float> numbers = new ArrayList<>();
        numbers.add(patternOneMaxValue(parserServiceResult.getText()));
        numbers.add(patternTwoMaxValue(parserServiceResult.getText()));
        @NotNull final Float maxYearValue = NumberUtil.findMaxFloat(numbers);
        if (maxYearValue == null) return null;
        return "опыт_в_годах_" + maxYearValue;
    }

    public static boolean isLooksNormal(@NotNull final Float value) {
        if(value > MAX_YEAR) return false;
        return true;
    }

}
