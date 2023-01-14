package ru.pyrinoff.chatjobparser.parser.salary.result;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.pyrinoff.chatjobparser.enumerated.model.dto.CurrencyEnum;

@Getter
@Setter
public class SalaryParserResult {

    private @Nullable Integer from;

    private @Nullable Integer to;

    private @Nullable CurrencyEnum currencyEnum;

    private @NotNull Boolean withPrediction = false;

}
