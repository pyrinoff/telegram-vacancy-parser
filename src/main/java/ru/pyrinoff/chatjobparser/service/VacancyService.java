package ru.pyrinoff.chatjobparser.service;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pyrinoff.chatjobparser.model.dto.Vacancy;
import ru.pyrinoff.chatjobparser.model.endpoint.DatasetRequest;
import ru.pyrinoff.chatjobparser.model.endpoint.DatasetResponse;
import ru.pyrinoff.chatjobparser.repository.VacancyRepository;
import ru.pyrinoff.chatjobparser.service.charts.PeriodCountChartService;
import ru.pyrinoff.chatjobparser.service.charts.SalaryCountChartService;

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

    @Getter
    @Autowired
    private @NotNull SalaryCountChartService salaryCountChartService;

    @Getter
    @Autowired
    private @NotNull PeriodCountChartService periodCountChartService;

    @Transactional
    public DatasetResponse getDataset(@NotNull final DatasetRequest datasetRequest) {
        //Set request
        @NotNull final DatasetResponse datasetResponse = new DatasetResponse();
        datasetResponse.setRequest(datasetRequest);
        datasetResponse.setSalaryCountChart(salaryCountChartService.getDataset(datasetRequest));
        datasetResponse.setDateCountChart(periodCountChartService.getDataset(datasetRequest));
        return datasetResponse;
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
