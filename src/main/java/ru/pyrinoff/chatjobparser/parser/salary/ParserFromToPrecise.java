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

@Order(10)
@Component
public class ParserFromToPrecise extends AbstractParser {
    //[\d \.]{1,7}
    public static @NotNull String PATTERN = "(от){0,1}"+CAN_BE_WHITESPACE+PATTERN_PART_ONE_BORDER+CAN_BE_WHITESPACE+PATTERN_PART_K+CAN_BE_WHITESPACE+PATTERN_PART_CURRENCY_NONREQ+CAN_BE_WHITESPACE
            +"(до|-|–| |—)"+CAN_BE_WHITESPACE+PATTERN_PART_ONE_BORDER+CAN_BE_WHITESPACE+PATTERN_PART_K+CAN_BE_WHITESPACE+PATTERN_PART_CURRENCY_NONREQ;

    @Override public @Nullable SalaryParserResult parse(@NotNull String text) {
        if(DEBUG) System.out.println("Parser: " + super.getClass().getSimpleName());
        return parseMethod(text, true);
    }

    public static @Nullable SalaryParserResult parseMethod(@NotNull final String text, final boolean preciseByCurrency) {
        @NotNull final Map<Integer, List<String>> matches = RegexUtils.getMatches(text, PATTERN);
        if (matches == null) return null;

        @NotNull final List<SalaryParserResult> salaryParserResultList = new ArrayList<>();

        for (int i = 0; i < matches.size(); i++) {
            if(DEBUG) System.out.println("Loop: " + i);
            @NotNull final SalaryParserResult salaryParserResult = new SalaryParserResult();

            @NotNull final List<String> oneMatch = matches.get(i);
            if(DEBUG) System.out.println(oneMatch);

            final @Nullable String fromWordString = oneMatch.get(1);
            final @Nullable String valueFromString = oneMatch.get(2);
            final @Nullable Integer valueFrom = IntegerUtils.parseInt(valueFromString);
            final @Nullable String thousandFrom = oneMatch.get(3);
            final @Nullable String currencyFrom = oneMatch.get(4);
            final @Nullable String toWordString = oneMatch.get(5);
            final @Nullable String valueToString = oneMatch.get(6);
            final @Nullable Integer valueTo = IntegerUtils.parseInt(valueToString);
            final @Nullable String thousandTo = oneMatch.get(7);
            final @Nullable String currencyTo = oneMatch.get(8);

            if (valueFrom == null || valueTo == null) {
                System.out.println("Required groups (valueFrom and valueTo) is null, must not be here. Is regex changed?");
                continue;
            }

            if (preciseByCurrency && (fromWordString == null && (toWordString == null))) { // || toWordString.equals(" ")))) {
                System.out.println("Required groups (fromWordString and toWordString) is null, must not be here. Is regex changed?");
                continue;
            }

            @Nullable final String currencyString = currencyFrom != null ? currencyFrom : currencyTo != null ? currencyTo : null;
            final boolean isK = (thousandFrom != null && !thousandFrom.isEmpty() || thousandTo != null && !thousandTo.isEmpty());
            @Nullable final CurrencyEnum currency = getCurrencyByString(currencyString);

            int valueFromInt = isK ? valueFrom * 1000 : valueFrom;
            int valueToInt = isK ? valueTo * 1000 : valueTo;

            salaryParserResult.setCurrencyEnum(currency);
            salaryParserResult.setFrom(valueFromInt);
            salaryParserResult.setTo(valueToInt);

            if(currency != null) {
                if(!isSalaryLooksNormal(currency, valueFromInt) || !isSalaryLooksNormal(currency, valueToInt)) {
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
        return this.getClass().getSimpleName() + ", PATTERN " + PATTERN;
    }

}