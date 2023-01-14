package ru.pyrinoff.chatjobparser.parser.salary;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.pyrinoff.chatjobparser.enumerated.model.dto.CurrencyEnum;
import ru.pyrinoff.chatjobparser.parser.salary.result.SalaryParserData;
import ru.pyrinoff.chatjobparser.parser.salary.result.SalaryParserResult;
import ru.pyrinoff.chatjobparser.util.RegexUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Order(20)
@Component
public class ParserFromOrToPrecise extends AbstractParser {

    @Getter
    public @NotNull String pattern = getPatternStatic();

    public static @NotNull String getPatternStatic() {
        return "(от|до){1}"+CAN_BE_WHITESPACE+PATTERN_PART_CURRENCY_NONREQ+PATTERN_PART_ONE_BORDER+CAN_BE_WHITESPACE+PATTERN_PART_K+CAN_BE_WHITESPACE+PATTERN_PART_CURRENCY_NONREQ;
    }

    @Override protected @Nullable SalaryParserData parseOneMatch(List<String> oneMatch) {
        return parseOneMatchStatic(oneMatch);
    }

    protected static @Nullable SalaryParserData parseOneMatchStatic(List<String> oneMatch) {
        final @Nullable String word = oneMatch.get(1);
        final @Nullable String currency = oneMatch.get(5) != null ? oneMatch.get(5) : oneMatch.get(2);
        final @Nullable String value = oneMatch.get(3);
        final @Nullable String thousand = oneMatch.get(4);

        if(word.equals("от")) {
            return new SalaryParserData()
                    .setWordFrom(word)
                    .setCurrencyFrom(currency)
                    .setValueFrom(value)
                    .setThousandFrom(thousand)
                    ;
        }
        else if(word.equals("до")) {
            return new SalaryParserData()
                    .setWordTo(word)
                    .setCurrencyTo(currency)
                    .setValueTo(value)
                    .setThousandTo(thousand)
            ;
        }
        return null;
    }

    @Override public @Nullable SalaryParserResult parse(@NotNull String text) {
        if (DEBUG) System.out.println("Parser: " + super.getClass().getSimpleName());
        return parseDefault(text, true, true, false);
    }

}
