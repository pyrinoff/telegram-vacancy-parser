package ru.pyrinoff.chatjobparser.component.parser.salary;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.pyrinoff.chatjobparser.component.parser.salary.result.SalaryParserData;
import ru.pyrinoff.chatjobparser.component.parser.salary.result.SalaryParserResult;

import java.util.List;

@Order(70)
@Component
public class ParserExactlyNonPrecise extends AbstractSalaryParser {

    @Getter
    public @NotNull String pattern = CAN_BE_WHITESPACE+PATTERN_PART_CURRENCY_NONREQ+PATTERN_PART_ONE_BORDER+CAN_BE_WHITESPACE+PATTERN_PART_K+CAN_BE_WHITESPACE+PATTERN_PART_CURRENCY_OR_WORD_REQ;

    @Override protected @Nullable SalaryParserData parseOneMatch(List<String> oneMatch) {
        return ParserExactlyPrecise.parseOneMatchStatic(oneMatch);
    }

    @Override
    public @Nullable SalaryParserResult parse(@NotNull String text) {
        if(DEBUG) System.out.println("Parser: " + this.getClass().getSimpleName());
        return parseDefault(text, false, false, false);
    }

}
