package ru.pyrinoff.chatjobparser.service;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pyrinoff.chatjobparser.model.dto.Vacancy;
import ru.pyrinoff.chatjobparser.repository.AbstractDTORepository;
import ru.pyrinoff.chatjobparser.repository.VacancyRepository;

@Service
public class VacancyService extends AbstractDTOService<Vacancy, VacancyRepository> {

    @Getter
    @Autowired
    private @NotNull VacancyRepository repository;

    @Override
    protected @NotNull AbstractDTORepository<Vacancy> getRepository() {
        return repository;
    }


}
