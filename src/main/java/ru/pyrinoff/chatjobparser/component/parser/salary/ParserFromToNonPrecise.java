package ru.pyrinoff.chatjobparser.component.parser.salary;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.pyrinoff.chatjobparser.component.parser.salary.result.SalaryParserData;
import ru.pyrinoff.chatjobparser.component.parser.salary.result.SalaryParserResult;

import java.util.List;

@Order(50)
@Component
public class ParserFromToNonPrecise extends AbstractSalaryParser {

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