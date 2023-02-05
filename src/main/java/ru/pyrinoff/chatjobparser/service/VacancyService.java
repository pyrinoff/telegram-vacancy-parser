package ru.pyrinoff.chatjobparser.service;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pyrinoff.chatjobparser.enumerated.dto.CurrencyEnum;
import ru.pyrinoff.chatjobparser.enumerated.dto.SqlOperatorEnum;
import ru.pyrinoff.chatjobparser.model.dto.Vacancy;
import ru.pyrinoff.chatjobparser.model.endpoint.DatasetRequest;
import ru.pyrinoff.chatjobparser.model.endpoint.DatasetResponse;
import ru.pyrinoff.chatjobparser.model.endpoint.OneLineRequest;
import ru.pyrinoff.chatjobparser.model.endpoint.SalarySettings;
import ru.pyrinoff.chatjobparser.repository.VacancyRepository;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class VacancyService {

    @Getter
    private long DB_ROWS_COUNT;

    @Getter
    private Date DB_LAST_VACANCY_DATE;

    @Getter
    private TreeSet<String> MARKERS;

    @Getter
    private TreeSet<String> WORDS;

    @PostConstruct
    public void postConstruct() {
        recalculateStatData();
    }

    public void recalculateStatData() {
        System.out.println("(Re)Calculating stat data...");
        DB_ROWS_COUNT = getRowsCount();
        DB_LAST_VACANCY_DATE = getLastVacancyDate();
        MARKERS = getMarkersList();
        WORDS = getWordsList();
    }

    @Getter
    @Autowired
    private @NotNull VacancyRepository repository;

/*    public DatasetResponse getDataset(@NotNull final DatasetRequest datasetRequest) {
        @NotNull final DatasetResponse datasetResponse = new DatasetResponse();
        //System.out.println(request);
        datasetResponse.setRequest(datasetRequest);
        datasetResponse.setDataset(new Object[][] {
                {"Зарплата", "Линия 1", "Линия 2", "Линия 3"},
                {50000, 0, 1, 0},
                {70000, 0, 1, 0},
                {90000, 1, 1, 0},
                {100000, 1, 6, 0},
                {110000, 0, 1, 0},
                {120000, 1, 2, 0},
                {130000, 0, 1, 0},
                {140000, 0, 5, 1}
        });
        return datasetResponse;
    }*/


    @Transactional
    public DatasetResponse getDataset(@NotNull final DatasetRequest datasetRequest) {

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
        Object[][] finalObject = convertListMapResultArray(resultLinesList);

        //Set request
        @NotNull final DatasetResponse datasetResponse = new DatasetResponse();
        datasetResponse.setRequest(datasetRequest);
        datasetResponse.setDataset(finalObject);
        return datasetResponse;
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

        @NotNull Map<Integer, Integer> statistic = repository.getStatistic(
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

    public void addAll(List<Vacancy> vacancies) {
        repository.saveAll(vacancies);
    }

    public long getRowsCount() {
        return repository.count();
    }

    public Date getLastVacancyDate() {
        @NotNull final Date lastVacancyDate = repository.getLastVacancyDate();
        return lastVacancyDate;
    }

    public @NotNull TreeSet<String> getMarkersList() {
        TreeSet<String> markers = repository.getMarkersList();
        if(markers == null) return new TreeSet<String>();
        return markers;
    }

    public @NotNull TreeSet<String> getWordsList() {
        TreeSet<String> words = repository.getWordsList();
        if(words == null) return new TreeSet<String>();
        return words;
    }

    public void removeAll() {
        repository.deleteAll();
    }

    public void add(@NotNull final Vacancy vacancy) {
        repository.save(vacancy);
    }

}
