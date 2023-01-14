package ru.pyrinoff.chatjobparser.parser.salary;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.pyrinoff.chatjobparser.enumerated.model.dto.CurrencyEnum;
import ru.pyrinoff.chatjobparser.parser.salary.result.SalaryParserResult;
import ru.pyrinoff.chatjobparser.util.IntegerUtils;
import ru.pyrinoff.chatjobparser.util.RegexUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Order(30)
@Component
public class ParserExactlyPrecise extends AbstractParser {

    public static @NotNull String PATTERN = CAN_BE_WHITESPACE+PATTERN_PART_CURRENCY_NONREQ+PATTERN_PART_ONE_BORDER+CAN_BE_WHITESPACE+PATTERN_PART_K+CAN_BE_WHITESPACE+PATTERN_PART_CURRENCY_REQ;
    public static @NotNull String PATTERN_NON_PRECISE = CAN_BE_WHITESPACE+PATTERN_PART_CURRENCY_NONREQ+PATTERN_PART_ONE_BORDER+CAN_BE_WHITESPACE+PATTERN_PART_K+CAN_BE_WHITESPACE+PATTERN_PART_CURRENCY_OR_WORD_REQ;

    @Override public @Nullable SalaryParserResult parse(@NotNull String text) {
        if(DEBUG) System.out.println("Parser: " + this.getClass().getSimpleName());
        return parseMethod(text, true);
    }

    public static @Nullable SalaryParserResult parseMethod(@NotNull final String text, final boolean preciseByCurrency) {
        @NotNull final Map<Integer, List<String>> matches = RegexUtils.getMatches(text, preciseByCurrency ? PATTERN : PATTERN_NON_PRECISE);
        if (matches == null) return null;

        @NotNull final List<SalaryParserResult> salaryParserResultList = new ArrayList<>();

        for (int i = 0; i < matches.size(); i++) {
            if(DEBUG) System.out.println("Loop: " + i);
            @NotNull final SalaryParserResult salaryParserResult = new SalaryParserResult();
            @NotNull final List<String> oneMatch = matches.get(i);
            if(DEBUG) System.out.println(oneMatch);

            final @Nullable String currencyOne = oneMatch.get(1);
            final @Nullable String valueString = oneMatch.get(2);
            final @Nullable Integer value = IntegerUtils.parseInt(valueString);
            final @Nullable String thousand = oneMatch.get(3);
            final @Nullable String currencyTwo = oneMatch.get(4);

            if (value == null) {
                System.out.println("Required groups (value) is null, must not be here. Is regex changed?");
                continue;
            }

            final boolean isK = thousand != null && !thousand.isEmpty();
            final @Nullable String currencyString = currencyOne != null ? currencyOne : currencyTwo;
            @Nullable final CurrencyEnum currency = getCurrencyByString(currencyString);

            final int valueInt = isK ? value * 1000 : value;

            salaryParserResult.setCurrencyEnum(currency);
            salaryParserResult.setFrom(valueInt);
            salaryParserResult.setTo(valueInt);

            if(currency != null) {
                if(!isSalaryLooksNormal(currency, valueInt)) {
                    System.out.println("Salary looks like innormal, skip!");
                    continue;
                }
                return salaryParserResult;
            }
            salaryParserResultList.add(salaryParserResult);
        }

        if(preciseByCurrency) return null;
        return predictCurrency(salaryParserResultList);

    }

    @Override public String toString() {
        return this.getClass().getSimpleName() + ", PATTERN " + PATTERN+"\nPATTERN_NON_PRECISE: "+PATTERN_NON_PRECISE;
    }

}
