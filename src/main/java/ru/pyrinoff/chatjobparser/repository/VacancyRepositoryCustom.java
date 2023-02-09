package ru.pyrinoff.chatjobparser.repository;

import org.jetbrains.annotations.NotNull;
import ru.pyrinoff.chatjobparser.enumerated.dto.CurrencyEnum;
import ru.pyrinoff.chatjobparser.enumerated.dto.SqlOperatorEnum;
import ru.pyrinoff.chatjobparser.enumerated.dto.TimePeriodEnum;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface VacancyRepositoryCustom {
    Map<Integer, Integer> getStatisticBySalary(List<String> markers, List<String> words, Date periodFrom, Date periodTo, Integer salaryFrom, Integer salaryTo, Boolean fromBorder, Boolean toBorder, Boolean bothBorders, Boolean allowPredicted, CurrencyEnum currency, SqlOperatorEnum markersOperator, SqlOperatorEnum wordsOperator);
    @NotNull Map<Date, Integer> getStatisticByTime(List<String> markers, List<String> words, Date periodFrom, Date periodTo, Integer salaryFrom, Integer salaryTo, Boolean fromBorder, Boolean toBorder, Boolean bothBorders, Boolean allowPredicted, CurrencyEnum currency, SqlOperatorEnum markersOperator, SqlOperatorEnum wordsOperator, TimePeriodEnum timePeriodEnum);

}
