package ru.pyrinoff.chatjobparser.component.parser.salary;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import ru.pyrinoff.chatjobparser.component.parser.salary.result.SalaryParserData;
import ru.pyrinoff.chatjobparser.component.parser.salary.result.SalaryParserResult;
import ru.pyrinoff.chatjobparser.enumerated.dto.CurrencyEnum;
import ru.pyrinoff.chatjobparser.util.NumberUtil;
import ru.pyrinoff.chatjobparser.util.RegexUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ru.pyrinoff.chatjobparser.util.NumberUtil.isValueInRange;

@Component
public abstract class AbstractSalaryParser {

    public static final @NotNull String CAN_BE_WHITESPACE = "[\\s]{0,1}";
    public static final @NotNull String PATTERN_PART_ONE_BORDER = "([1-9]{1}[\\d]{1,5}|[1-9]{1}[.]{0,1}[\\d]{0,1})";
    public static final @NotNull String PATTERN_PART_K = "(к|k|тыс\\.|тыс|т\\.|т){0,1}";
    public static final @NotNull String PATTERN_PART_CURRENCY = "(\\$|USD|руб\\.|руб|р|р\\.|rub|eur|долларов|€|euro|usd|rur|евро)";
    public static final @NotNull String PATTERN_PART_CURRENCY_REQ = PATTERN_PART_CURRENCY + "{1}";
    public static final @NotNull String PATTERN_PART_CURRENCY_NONREQ = PATTERN_PART_CURRENCY + "{0,1}";
    public static final @NotNull String PATTERN_PART_CURRENCY_OR_WORD_REQ = "(\\$|USD|руб\\.|руб|р|р\\.|rub|eur|долларов|€|euro|usd|rur|net|нетто|на руки|в месяц|месяц|евро){1}";

    public static final @NotNull String TO_PATTERN = "(до|-|–| |—|-)";

    public static boolean DEBUG = false;

    protected static @Nullable CurrencyEnum getCurrencyByString(@Nullable final String currencyString) {
        if (currencyString == null || currencyString.isEmpty()) return null;
        if (
                currencyString.equals("$")
                        || currencyString.equals("usd")
                        || currencyString.equals("eur")
                        || currencyString.equals("euro")
                        || currencyString.equals("евро")
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

    protected static boolean isSalaryLooksNormal(
            @NotNull final CurrencyEnum currency,
            @NotNull final Integer value,
            boolean smallRange) {
        if(DEBUG) System.out.println("Check salary for range, currency "+currency+", value: "+value+", small range: "+smallRange);
        if (smallRange) {
            if (currency == CurrencyEnum.RUB && isValueInRange(value, 15000, 450000)) return true;
            if (currency == CurrencyEnum.USD && isValueInRange(value, 1000, 7000)) return true;
        } else {
            if (currency == CurrencyEnum.RUB && isValueInRange(value, 15000, 800000)) return true;
            if (currency == CurrencyEnum.USD && isValueInRange(value, 300, 12000)) return true;
        }
        if (DEBUG) System.out.println("Salary looks like NOT normal!");
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
        if(DEBUG) System.out.println("PATTERN: "+pattern);

        @NotNull final Map<Integer, List<String>> matches = RegexUtils.getMatches(text, pattern);
        if (matches == null) return null;
        if(DEBUG) System.out.println(matches);

        @NotNull final List<SalaryParserResult> salaryParserResultList = new ArrayList<>();

        for (int i = 0; i < matches.size(); i++) {
            if (DEBUG) System.out.println("Loop: " + i);
            @NotNull final SalaryParserResult salaryParserResult = new SalaryParserResult();

            @Nullable final SalaryParserData salaryParserData = parseOneMatch(matches.get(i));
            if (salaryParserData == null) {
                System.out.println("Cant parse that match... Skip");
                continue;
            }
            if (DEBUG) System.out.println(salaryParserData);

            //final @Nullable Integer valueFrom = IntegerUtils.parseInt(valueFromString);

            if (salaryParserData.getValueFrom() == null && salaryParserData.getValueTo() == null) {
                System.out.println("No values found! Why??? Skip.");
                continue;
            }

            if (isRequiredFromTo && salaryParserData.getWordFrom() == null && salaryParserData.getWordTo() == null) {
                if (DEBUG) System.out.println("Skip by strict getFromWord/getToWord");
                continue;
            }

            @Nullable final String currencyString = salaryParserData.getCurrencyTo() != null ? salaryParserData.getCurrencyTo() : salaryParserData.getCurrencyFrom();
            @Nullable final CurrencyEnum currency = getCurrencyByString(currencyString);
            if (isRequiredCurrency && (currencyString == null || currency == null)) {
                if (DEBUG) System.out.println("Skip by strict currencyFrom/currencyTo/currency");
                continue;
            }
            if(DEBUG) System.out.println("Final currency: " + currency);

            @Nullable final String thousandString = salaryParserData.getThousandTo() != null ? salaryParserData.getThousandTo() : salaryParserData.getThousandFrom();
            if (isRequiredThousand && (thousandString == null || thousandString.isEmpty())) {
                if (DEBUG) System.out.println("Skip by strict thousand");
                continue;
            }

            final boolean isK = (thousandString != null && !thousandString.isEmpty());

            Integer valueFromInt = null;
            Integer valueToInt = null;

            if (salaryParserData.getValueFrom() != null) {
                if(DEBUG) System.out.println("Checking 'From' value for range...");
                @Nullable Float valueFromFloat = NumberUtil.parseFloat(salaryParserData.getValueFrom(), 7);
                if (valueFromFloat == null) {
                    System.out.println("Float parsing error??!!");
                    continue;
                }
                if (isK) valueFromFloat *= 1000;
                valueFromInt = Math.round(valueFromFloat);
            }

            if (salaryParserData.getValueTo() != null) {
                if(DEBUG) System.out.println("Checking 'To' value for range...");
                @Nullable Float valueToFloat = NumberUtil.parseFloat(salaryParserData.getValueTo(), 7);
                if (valueToFloat == null) {
                    System.out.println("Float parsing error??!!");
                    continue;
                }
                if (isK) valueToFloat *= 1000;
                valueToInt = Math.round(valueToFloat);
            }

            if (valueFromInt != null && valueToInt != null && valueFromInt > valueToInt) {
                if (DEBUG) System.out.println("From greater than to, skip it!");
                continue;
            }

            salaryParserResult.setCurrencyEnum(currency);
            salaryParserResult.setFrom(valueFromInt);
            salaryParserResult.setTo(valueToInt);

            if (currency != null) {
                if (valueFromInt != null && !isSalaryLooksNormal(currency, valueFromInt, false)
                        || valueToInt != null && !isSalaryLooksNormal(currency, valueToInt, false)
                ) {
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
            for (@NotNull final CurrencyEnum oneCurrency : CurrencyEnum.values()) {
                if (oneSalaryParserResult.getFrom() != null && !isSalaryLooksNormal(oneCurrency, oneSalaryParserResult.getFrom(), true))
                    continue;
                if (oneSalaryParserResult.getTo() != null && !isSalaryLooksNormal(oneCurrency, oneSalaryParserResult.getTo(), true))
                    continue;
                if (DEBUG) System.out.println("Salary DOES looks like " + oneCurrency + "!");
                oneSalaryParserResult.setCurrencyEnum(oneCurrency);
                oneSalaryParserResult.setWithPrediction(true);
                return oneSalaryParserResult;
            }
        }
        return null;
    }

    @Override public String toString() {
        return this.getClass().getSimpleName() + ", REGEX:\n" + getPattern();
    }

}
