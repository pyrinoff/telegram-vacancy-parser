package ru.pyrinoff.chatjobparser.parser.salary;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.pyrinoff.chatjobparser.parser.salary.result.SalaryParserData;
import ru.pyrinoff.chatjobparser.parser.salary.result.SalaryParserResult;

import java.util.List;

@Order(10)
@Component
public class ParserFromToPrecise extends AbstractSalaryParser {

    @Getter
    public @NotNull String pattern = getPatternStatic();

    public static @NotNull String getPatternStatic() {
        return "(от){0,1}" + CAN_BE_WHITESPACE + PATTERN_PART_CURRENCY_NONREQ + CAN_BE_WHITESPACE + PATTERN_PART_ONE_BORDER + CAN_BE_WHITESPACE + PATTERN_PART_K + CAN_BE_WHITESPACE + PATTERN_PART_CURRENCY_NONREQ + CAN_BE_WHITESPACE
                + TO_PATTERN + CAN_BE_WHITESPACE + PATTERN_PART_ONE_BORDER + CAN_BE_WHITESPACE + PATTERN_PART_K + CAN_BE_WHITESPACE + PATTERN_PART_CURRENCY_NONREQ;
    }

    @Override protected @Nullable SalaryParserData parseOneMatch(List<String> oneMatch) {
        return parseOneMatchStatic(oneMatch);
    }

    protected static @Nullable SalaryParserData parseOneMatchStatic(List<String> oneMatch) {
        return new SalaryParserData()
                .setWordFrom(oneMatch.get(1))
                .setValueFrom(oneMatch.get(3))
                .setThousandFrom(oneMatch.get(4))
                .setCurrencyFrom(oneMatch.get(5) != null ? oneMatch.get(5) : oneMatch.get(2))
                .setWordTo(oneMatch.get(6))
                .setValueTo(oneMatch.get(7))
                .setThousandTo(oneMatch.get(8))
                .setCurrencyTo(oneMatch.get(9));
    }

    @Override public @Nullable SalaryParserResult parse(@NotNull String text) {
        if (DEBUG) System.out.println("Parser: " + super.getClass().getSimpleName());
        return parseDefault(text, true, true, false);
    }

}