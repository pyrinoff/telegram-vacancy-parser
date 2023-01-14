package ru.pyrinoff.chatjobparser.parser.salary;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.pyrinoff.chatjobparser.enumerated.model.dto.CurrencyEnum;
import ru.pyrinoff.chatjobparser.parser.salary.result.SalaryParserData;
import ru.pyrinoff.chatjobparser.parser.salary.result.SalaryParserResult;
import ru.pyrinoff.chatjobparser.util.IntegerUtils;
import ru.pyrinoff.chatjobparser.util.RegexUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Order(30)
@Component
public class ParserExactlyPrecise extends AbstractParser {

    @Getter
    public @NotNull String pattern = CAN_BE_WHITESPACE + PATTERN_PART_CURRENCY_NONREQ + PATTERN_PART_ONE_BORDER + CAN_BE_WHITESPACE + PATTERN_PART_K + CAN_BE_WHITESPACE + PATTERN_PART_CURRENCY_REQ;

    protected static @Nullable SalaryParserData parseOneMatchStatic(List<String> oneMatch) {
        final @Nullable String currency = oneMatch.get(4) != null ? oneMatch.get(4) : oneMatch.get(1);
        final @Nullable String value = oneMatch.get(2);
        final @Nullable String thousand = oneMatch.get(3);

        return new SalaryParserData()
                .setCurrencyFrom(currency)
                .setValueFrom(value)
                .setThousandFrom(thousand)
                .setCurrencyTo(currency)
                .setValueTo(value)
                .setThousandTo(thousand)
                ;
    }

    @Override
    protected @Nullable SalaryParserData parseOneMatch(List<String> oneMatch) {
        return parseOneMatchStatic(oneMatch);
    }

    @Override
    public @Nullable SalaryParserResult parse(@NotNull String text) {
        if (DEBUG) System.out.println("Parser: " + this.getClass().getSimpleName());
        return parseDefault(text, false, true, false);
    }

}
