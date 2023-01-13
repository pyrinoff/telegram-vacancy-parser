package ru.pyrinoff.chatjobparser.parser.salary;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.pyrinoff.chatjobparser.enumerated.model.dto.CurrencyEnum;
import ru.pyrinoff.chatjobparser.parser.salary.result.SalaryParserResult;
import ru.pyrinoff.chatjobparser.util.IntegerUtils;
import ru.pyrinoff.chatjobparser.util.RegexUtils;

import java.util.List;
import java.util.Map;

import static ru.pyrinoff.chatjobparser.util.IntegerUtils.isValueInRange;

@Order(20)
@Component
public abstract class AbstractParser {

    public static final boolean DEBUG = false;

    abstract public @Nullable SalaryParserResult parse(@NotNull String text);

    protected static @Nullable CurrencyEnum getCurrencyByString(@Nullable final String currencyString) {
        if (currencyString == null || currencyString.isEmpty()) return null;
        if (currencyString.equals("$") || currencyString.equals("USD")) return CurrencyEnum.USD;
        if (currencyString.equals("руб") || currencyString.equals("р") || currencyString.equals("р.") || currencyString.equals("rub"))
            return CurrencyEnum.RUB;
        return null;
    }

    protected static boolean isSalaryLooksNormal(@NotNull final CurrencyEnum currency, @NotNull final Integer value) {
        if(currency == CurrencyEnum.RUB && isValueInRange(value, 10000, 1000000)) return true;
        if(currency == CurrencyEnum.USD && isValueInRange(value, 300, 10000)) return true;
        return false;
    }

    protected static @Nullable SalaryParserResult predictCurrency(@NotNull final List<SalaryParserResult> salaryParserResultList) {
        if(DEBUG) System.out.println("PREDICTING...");
        for (@NotNull final SalaryParserResult oneSalaryParserResult : salaryParserResultList) {
            if( (oneSalaryParserResult.getFrom() != null && isSalaryLooksNormal(CurrencyEnum.RUB, oneSalaryParserResult.getFrom()))
                    || (oneSalaryParserResult.getTo() != null && isSalaryLooksNormal(CurrencyEnum.RUB, oneSalaryParserResult.getTo()))
            ) {
                if(DEBUG) System.out.println("Salary DOES looks like RUB!");
                oneSalaryParserResult.setCurrencyEnum(CurrencyEnum.RUB);
                return oneSalaryParserResult;
            }
            if( (oneSalaryParserResult.getFrom() != null && isSalaryLooksNormal(CurrencyEnum.USD, oneSalaryParserResult.getFrom()))
                    || (oneSalaryParserResult.getTo() != null && isSalaryLooksNormal(CurrencyEnum.USD, oneSalaryParserResult.getTo()))
            ) {
                if(oneSalaryParserResult.isHasDotInValue()) {
                    if(DEBUG) System.out.println("Salary DOES looks like USD, BUT HAS DOT!");
                    continue;
                }
                if(DEBUG) System.out.println("Salary DOES looks like USD!");
                oneSalaryParserResult.setCurrencyEnum(CurrencyEnum.USD);
                return oneSalaryParserResult;
            }
        }
        return null;
    }

}
