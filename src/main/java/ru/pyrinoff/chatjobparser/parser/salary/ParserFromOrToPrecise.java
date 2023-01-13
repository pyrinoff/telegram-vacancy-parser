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

@Order(20)
@Component
public class ParserFromOrToPrecise extends AbstractParser {

    public static @NotNull String PATTERN = "(от|до){1}[\\s]{0,1}(\\$|USD|руб|р|р\\.|rub){0,1}([\\d]{1}[\\d ]{1,6})(к|k|тыс\\.|тыс|т\\.р\\.|тр){0,1}[\\s]{0,1}(\\$|USD|руб|р|р\\.|rub){0,1}[\\s]{0,1}";

    @Override public @Nullable SalaryParserResult parse(@NotNull String text) {
        if(DEBUG) System.out.println("Parser: " + this.getClass().getSimpleName());
        return parseMethod(text, true);
    }

    public static @Nullable SalaryParserResult parseMethod(@NotNull final String text, final boolean preciseByCurrency) {
        @NotNull final Map<Integer, List<String>> matches = RegexUtils.getMatches(text, ParserFromOrToPrecise.PATTERN);
        if (matches == null) return null;

        @NotNull final List<SalaryParserResult> salaryParserResultList = new ArrayList<>();

        for (int i = 0; i < matches.size(); i++) {
            if(DEBUG) System.out.println("Loop: " + i);
            @NotNull final SalaryParserResult salaryParserResult = new SalaryParserResult();
            @NotNull final List<String> oneMatch = matches.get(i);
            if(DEBUG) System.out.println(oneMatch);


            final @Nullable String fromOrTo = oneMatch.get(1);
            final @Nullable String currencyOne = oneMatch.get(2);
            final @Nullable String valueString = oneMatch.get(3);
            final @Nullable Integer value = IntegerUtils.parseInt(valueString);
            final @Nullable String thousand = oneMatch.get(4);
            final @Nullable String currencyTwo = oneMatch.get(5);

            if (value == null) {
                System.out.println("Required groups (value) is null, must not be here. Is regex changed?");
                continue;
            }
            if (fromOrTo == null) {
                System.out.println("Required groups (fromOrTo) is null, must not be here. Is regex changed?");
                continue;
            }

            final boolean isK = thousand != null && !thousand.isEmpty();
            final @Nullable String currencyString = currencyOne != null ? currencyOne : currencyTwo;
            @Nullable final CurrencyEnum currency = getCurrencyByString(currencyString);
            final boolean isFrom = fromOrTo.equals("от");
            final boolean isTo = fromOrTo.equals("до");
            final boolean hasDotInValue = valueString.contains(".") || valueString.contains(".");

            final int valueInt = isK ? value * 1000 : value;

            salaryParserResult.setCurrencyEnum(currency);
            if(isFrom) salaryParserResult.setFrom(valueInt);
            if(isTo) salaryParserResult.setTo(valueInt);
            salaryParserResult.setHasDotInValue(hasDotInValue);

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

}
