package ru.pyrinoff.chatjobparser.repository;

import org.springframework.stereotype.Repository;
import ru.pyrinoff.chatjobparser.model.dto.Vacancy;

@Repository
public class VacancyRepository extends AbstractDTORepository<Vacancy> {

    @Override
    protected Class<Vacancy> getEntityClass() {
        return Vacancy.class;
    }

}
