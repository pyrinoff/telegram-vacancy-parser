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

@Order(50)
@Component
public class ParserFromToNonPrecise extends AbstractParser {

    @Getter
    public @NotNull String pattern = ParserFromToPrecise.getPatternStatic();

    @Override protected @Nullable SalaryParserData parseOneMatch(List<String> oneMatch) {
        return ParserFromToPrecise.parseOneMatchStatic(oneMatch);
    }

    @Override public @Nullable SalaryParserResult parse(@NotNull String text) {
        if (DEBUG) System.out.println("Parser: " + super.getClass().getSimpleName());
        return parseDefault(text, true, false, false);
    }
}