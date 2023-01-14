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

@Order(60)
@Component
public class ParserFromOrToNonPrecise extends AbstractParser {

    @Getter
    public @NotNull String pattern = ParserFromOrToPrecise.getPatternStatic();

    @Override public @Nullable SalaryParserResult parse(@NotNull String text) {
        if(DEBUG) System.out.println("Parser: " + this.getClass().getSimpleName());
        return parseDefault(text, true, false, false);
    }

    @Override protected @Nullable SalaryParserData parseOneMatch(@NotNull final List<String> oneMatch) {
        return ParserFromOrToPrecise.parseOneMatchStatic(oneMatch);
    }

}
