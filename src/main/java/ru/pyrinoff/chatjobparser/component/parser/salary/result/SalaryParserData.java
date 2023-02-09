package ru.pyrinoff.chatjobparser.component.parser.salary.result;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@Accessors(chain = true)
public class SalaryParserData {

    private @Nullable String wordFrom;

    private @Nullable String valueFrom;

    private @Nullable String thousandFrom;

    private @Nullable String currencyFrom;

    private @Nullable String wordTo;

    private @Nullable String valueTo;

    private @Nullable String thousandTo;

    private @Nullable String currencyTo;

    @Override public String toString() {
        return "SalaryParserData{" +
                "wordFrom='" + wordFrom + '\'' +
                ", valueFrom='" + valueFrom + '\'' +
                ", thousandFrom='" + thousandFrom + '\'' +
                ", currencyFrom='" + currencyFrom + '\'' +
                ", wordTo='" + wordTo + '\'' +
                ", valueTo='" + valueTo + '\'' +
                ", thousandTo='" + thousandTo + '\'' +
                ", currencyTo='" + currencyTo + '\'' +
                '}';
    }

}
