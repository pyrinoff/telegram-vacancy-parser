package ru.pyrinoff.chatjobparser.repository;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.pyrinoff.chatjobparser.enumerated.model.dto.CurrencyEnum;
import ru.pyrinoff.chatjobparser.model.dto.Vacancy;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, String>, VacancyRepositoryCustom {

    @Query("SELECT MAX(v.date) FROM Vacancy v")
    Date getLastVacancyDate();

}
