package ru.pyrinoff.chatjobparser.service;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pyrinoff.chatjobparser.model.dto.Vacancy;
import ru.pyrinoff.chatjobparser.repository.AbstractDTORepository;
import ru.pyrinoff.chatjobparser.repository.VacancyRepository;

import java.util.List;

@Service
public class VacancyService extends AbstractDTOService<Vacancy, VacancyRepository> {

    @Getter
    @Autowired
    private @NotNull VacancyRepository repository;

    @Override
    protected @NotNull AbstractDTORepository<Vacancy> getRepository() {
        return repository;
    }

    @Transactional
    public void addAll(List<Vacancy> vacancies) {
        if(vacancies == null || vacancies.isEmpty()) return;
        vacancies.forEach(this::add); //TODO оптимизировать мб?
    }

}
