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
import java.util.*;


@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, String>, VacancyRepositoryCustom {

    //@Query("SELECT MAX(v.date) FROM Vacancy v")
    @Query(value="SELECT MAX(v.date) FROM vacancy v", nativeQuery = true)
    Date getLastVacancyDate();

    @Query(value="SELECT DISTINCT markers FROM vacancy_markers", nativeQuery = true)
    TreeSet<String> getMarkersList();

    @Query(value="SELECT DISTINCT words FROM vacancy_words", nativeQuery = true)
    TreeSet<String> getWordsList();

    //@Query(value="DELETE FROM vacancy_markers; DELETE from vacancy_words; DELETE FROM vacancy", nativeQuery = true)
    //void removeALl();


}
