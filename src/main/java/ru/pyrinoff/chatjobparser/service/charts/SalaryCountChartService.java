package ru.pyrinoff.chatjobparser.service.charts;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.pyrinoff.chatjobparser.enumerated.dto.CurrencyEnum;
import ru.pyrinoff.chatjobparser.enumerated.dto.SqlOperatorEnum;
import ru.pyrinoff.chatjobparser.enumerated.dto.TimePeriodEnum;
import ru.pyrinoff.chatjobparser.model.endpoint.DatasetRequest;
import ru.pyrinoff.chatjobparser.model.endpoint.OneLineRequest;
import ru.pyrinoff.chatjobparser.model.endpoint.SalarySettings;
import ru.pyrinoff.chatjobparser.repository.VacancyRepository;

import java.util.*;

@Component
public class SalaryCountChartService {

    @Getter
    @Autowired
    private VacancyRepository repository;

    public Object[][] getDataset(@NotNull final DatasetRequest datasetRequest) {

        //Get lines
        @NotNull final List<OneLineRequest> lines = datasetRequest.getLines();

        //Result lists will be in format List<Map<Integer, Integer>>: Map with salary => vacancyCount for each line row
        List<Map<Integer, Integer>> resultLinesList = new ArrayList<>();

        //Get stats for each line
        for(int i = 0; i< lines.size(); i++) {
            if(lines.get(i)==null) { //skip
                resultLinesList.add(new HashMap<>(0));
                continue;
            }
            resultLinesList.add(getOneLineStat(datasetRequest, i));
        }

        //Convert stat to final array
        return convertListMapResultArray(resultLinesList);
    }

    public Map<Integer, Integer> getOneLineStat(@NotNull final DatasetRequest request, final int index) {
        if(request.getLines().size() <= index) throw new IndexOutOfBoundsException("Dont have line with index: "+index);
        @NotNull final OneLineRequest oneLine = request.getLines().get(index);

        @Nullable final List<String> markers = oneLine.getMarkers() != null ? oneLine.getMarkers() : Collections.emptyList();
        @NotNull final SqlOperatorEnum markersOperator = oneLine.getMarkersOperator();
        @Nullable final List<String> words = oneLine.getWords() != null ? oneLine.getWords() : Collections.emptyList();
        @NotNull final SqlOperatorEnum wordsOperator = oneLine.getWordsOperator();
        @Nullable final Date periodFrom = oneLine.getPeriodFrom();
        @Nullable final Date periodTo = oneLine.getPeriodTo();
        @NotNull final SalarySettings salary = oneLine.getSalary();

        @Nullable final Integer salaryFrom = salary.getSalaryFrom();
        @Nullable final Integer salaryTo = salary.getSalaryTo();
        @NotNull final Boolean fromBorder = salary.getFromBorder();
        @NotNull final Boolean toBorder = salary.getToBorder();
        @NotNull final Boolean bothBorders = salary.getBothBorders();
        @NotNull final Boolean allowPredicted = salary.getAllowPredicted();

        @NotNull final CurrencyEnum currency = request.getSalaryCurrency();

        @NotNull Map<Integer, Integer> statistic = repository.getStatisticBySalary(
                markers, words, periodFrom, periodTo, salaryFrom, salaryTo, fromBorder, toBorder, bothBorders, allowPredicted,
                currency, markersOperator, wordsOperator
        );
        if(statistic.size() == 0) statistic.put(0,0); //отправляем нули, если ничего не найдено
        return statistic;
    }

    //Traverse List<Map<Integer,Integer>> to Object[][]
    public static Object[][] convertListMapResultArray(@NotNull final List<Map<Integer, Integer>> resultLinesList) {
        @NotNull final TreeSet<Integer> salaries = new TreeSet<>();
        resultLinesList.forEach(oneLineMap -> salaries.addAll(oneLineMap.keySet()));
        int salariesCount = salaries.size();
        int rowCount = salariesCount + 1;
        int linesCount = resultLinesList.size();
        int entitiesPerRowCount = linesCount + 1;

        @NotNull final Object[][] finalDataset = new Object[rowCount][entitiesPerRowCount];

        int index = 0;

        //Form headers
        final List<String> headers = new ArrayList<>();
        headers.add("Зарплата");
        for (int i = 0; i < resultLinesList.size(); i++) headers.add("Линия " + (i + 1));
        finalDataset[index++] = headers.toArray();

        //Form data
        //0 => [50000=>1],
        //1 => [50000=>1]
        //to [50000, 1, 1]
        for (Integer oneSalary : salaries) {
            final List<Integer> oneLine = new ArrayList<>();
            oneLine.add(oneSalary);
            for (int j = 0; j < linesCount; j++) {
                oneLine.add(resultLinesList.get(j).getOrDefault(oneSalary, 0));
            }
            finalDataset[index++] = oneLine.toArray();
        }

        return finalDataset;
    }


}
