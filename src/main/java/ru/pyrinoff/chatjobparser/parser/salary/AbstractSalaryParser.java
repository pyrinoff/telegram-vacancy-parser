package ru.pyrinoff.chatjobparser.parser.salary;

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

import static ru.pyrinoff.chatjobparser.util.IntegerUtils.isValueInRange;

@Component
public abstract class AbstractSalaryParser {

    public static final @NotNull String CAN_BE_WHITESPACE = "[\\s]{0,1}";
    public static final @NotNull String PATTERN_PART_ONE_BORDER = "([1-9]{1}[\\d]{1,5}|[1-9]{1}[.]{0,1}[\\d]{0,1})";
    public static final @NotNull String PATTERN_PART_K = "(к|k|тыс\\.|тыс|т\\.|т){0,1}";
    public static final @NotNull String PATTERN_PART_CURRENCY = "(\\$|USD|руб\\.|руб|р|р\\.|rub|eur|долларов|€|euro|usd|rur)";
    public static final @NotNull String PATTERN_PART_CURRENCY_REQ = PATTERN_PART_CURRENCY + "{1}";
    public static final @NotNull String PATTERN_PART_CURRENCY_NONREQ = PATTERN_PART_CURRENCY + "{0,1}";
    public static final @NotNull String PATTERN_PART_CURRENCY_OR_WORD_REQ = "(\\$|USD|руб\\.|руб|р|р\\.|rub|eur|долларов|€|euro|usd|rur|net|нетто|на руки|в месяц|месяц){1}";
    public static volatile boolean DEBUG = false;

    protected static @Nullable CurrencyEnum getCurrencyByString(@Nullable final String currencyString) {
        if (currencyString == null || currencyString.isEmpty()) return null;
        if (
                currencyString.equals("$")
                        || currencyString.equals("usd")
                        || currencyString.equals("eur")
                        || currencyString.equals("euro")
                        || currencyString.startsWith("доллар")
                        || currencyString.startsWith("€")
        )
            return CurrencyEnum.USD;
        //#TODO CHANGE THIS TO ARRAY
        if (currencyString.equals("руб.") || currencyString.equals("руб") || currencyString.equals("р") || currencyString.equals("р.")
                || currencyString.equals("rub")
                || currencyString.equals("т.р.")
        )
            return CurrencyEnum.RUB;
        return null;
    }

    protected static boolean isSalaryLooksNormal(@NotNull final CurrencyEnum currency, @NotNull final Integer value) {
        if (currency == CurrencyEnum.RUB && isValueInRange(value, 15000, 1000000)) return true;
        if (currency == CurrencyEnum.USD && isValueInRange(value, 300, 10000)) return true;
        return false;
    }

    abstract public @Nullable SalaryParserResult parse(@NotNull String text);

    protected abstract @NotNull String getPattern();

    protected abstract @Nullable SalaryParserData parseOneMatch(List<String> oneMatch);

    protected @Nullable SalaryParserResult parseDefault(
            @NotNull final String text
            , boolean isRequiredFromTo
            , boolean isRequiredCurrency
            , boolean isRequiredThousand
    ) {
        @NotNull final String pattern = getPattern();

        @NotNull final Map<Integer, List<String>> matches = RegexUtils.getMatches(text, pattern);
        if (matches == null) return null;

        @NotNull final List<SalaryParserResult> salaryParserResultList = new ArrayList<>();

        for (int i = 0; i < matches.size(); i++) {
            if (DEBUG) System.out.println("Loop: " + i);
            @NotNull final SalaryParserResult salaryParserResult = new SalaryParserResult();

            @Nullable final SalaryParserData salaryParserData = parseOneMatch(matches.get(i));
            if(salaryParserData == null) {
                System.out.println("Cant parse that match... Skip");
                continue;
            }
            if (DEBUG) System.out.println(salaryParserData);

            //final @Nullable Integer valueFrom = IntegerUtils.parseInt(valueFromString);

            if (salaryParserData.getValueFrom() == null && salaryParserData.getValueTo() == null) {
                System.out.println("No values found! Skip.");
                continue;
            }

            if (isRequiredFromTo && salaryParserData.getWordFrom() == null && salaryParserData.getWordTo() == null) {
                System.out.println("Skip by strict getFromWord/getToWord");
                continue;
            }

            @Nullable final String currencyString = salaryParserData.getCurrencyTo() != null ? salaryParserData.getCurrencyTo() : salaryParserData.getCurrencyFrom();
            @Nullable final CurrencyEnum currency = getCurrencyByString(currencyString);
            if (isRequiredCurrency && (currencyString == null || currency == null)) {
                System.out.println("Skip by strict currencyFrom/currencyTo/currency");
                continue;
            }

            @Nullable final String thousandString = salaryParserData.getThousandTo() != null ? salaryParserData.getThousandTo() : salaryParserData.getThousandFrom();
            if (isRequiredThousand && (thousandString == null || thousandString.isEmpty())) {
                System.out.println("Skip by strict thousand");
                continue;
            }

            final boolean isK = (thousandString != null && !thousandString.isEmpty());

            Integer valueFromInt = null;
            Integer valueToInt = null;

            if (salaryParserData.getValueFrom() != null) {
                @Nullable Float valueFromFloat = IntegerUtils.parseFloat(salaryParserData.getValueFrom());
                if(valueFromFloat == null) {
                    System.out.println("Float parsing error??!!");
                    continue;
                }
                if (isK) valueFromFloat *= 1000;
                valueFromInt = Math.round(valueFromFloat);
            }

            if (salaryParserData.getValueTo() != null) {
                @Nullable Float valueToFloat = IntegerUtils.parseFloat(salaryParserData.getValueTo());
                if(valueToFloat == null) {
                    System.out.println("Float parsing error??!!");
                    continue;
                }
                if (isK) valueToFloat *= 1000;
                valueToInt = Math.round(valueToFloat);
            }

            salaryParserResult.setCurrencyEnum(currency);
            salaryParserResult.setFrom(valueFromInt);
            salaryParserResult.setTo(valueToInt);

            if (currency != null) {
                if (valueFromInt != null && !isSalaryLooksNormal(currency, valueFromInt)
                        || valueToInt != null && !isSalaryLooksNormal(currency, valueToInt)
                ) {
                    System.out.println("Salary looks like innormal, skip!");
                    continue;
                }
                //found correct result
                return salaryParserResult;
            }
            salaryParserResultList.add(salaryParserResult);
        }

        //Not found any - exit
        if (isRequiredCurrency) return null;

        //Or try to predict, then return
        return predictCurrency(salaryParserResultList);
    }

    protected static @Nullable SalaryParserResult predictCurrency(@NotNull final List<SalaryParserResult> salaryParserResultList) {
        if (DEBUG) System.out.println("Predicting currency...");
        for (@NotNull final SalaryParserResult oneSalaryParserResult : salaryParserResultList) {
            if ((oneSalaryParserResult.getFrom() != null && isSalaryLooksNormal(CurrencyEnum.RUB, oneSalaryParserResult.getFrom()))
                    || (oneSalaryParserResult.getTo() != null && isSalaryLooksNormal(CurrencyEnum.RUB, oneSalaryParserResult.getTo()))
            ) {
                if (DEBUG) System.out.println("Salary DOES looks like RUB!");
                oneSalaryParserResult.setCurrencyEnum(CurrencyEnum.RUB);
                oneSalaryParserResult.setWithPrediction(true);
                return oneSalaryParserResult;
            }
            if ((oneSalaryParserResult.getFrom() != null && isSalaryLooksNormal(CurrencyEnum.USD, oneSalaryParserResult.getFrom()))
                    || (oneSalaryParserResult.getTo() != null && isSalaryLooksNormal(CurrencyEnum.USD, oneSalaryParserResult.getTo()))
            ) {
                if (DEBUG) System.out.println("Salary DOES looks like USD!");
                oneSalaryParserResult.setCurrencyEnum(CurrencyEnum.USD);
                oneSalaryParserResult.setWithPrediction(true);
                return oneSalaryParserResult;
            }
        }
        return null;
    }

    @Override public String toString() {
        return this.getClass().getSimpleName()+", REGEX:\n"+getPattern();
    }

}
