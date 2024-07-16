package ru.pyrinoff.chatjobparser.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import ru.pyrinoff.chatjobparser.enumerated.dto.CurrencyEnum;
import ru.pyrinoff.chatjobparser.enumerated.dto.SqlOperatorEnum;
import ru.pyrinoff.chatjobparser.enumerated.dto.TimePeriodEnum;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class VacancyRepositoryImpl implements VacancyRepositoryCustom  {

    @PersistenceContext
    private EntityManager entityManager;

    public Map<Integer, Integer> getStatisticBySalary(
            @NotNull final List<String> markers,
            @NotNull final List<String> words,
            @Nullable final Date periodFrom,
            @Nullable final Date periodTo,
            @Nullable final Integer salaryFrom,
            @Nullable final Integer salaryTo,
            @NotNull final Boolean fromBorder,
            @NotNull final Boolean toBorder,
            @NotNull final Boolean bothBorders,
            @NotNull final Boolean allowPredicted,
            @NotNull final CurrencyEnum currency,
            @NotNull final SqlOperatorEnum markersOperator,
            @NotNull final SqlOperatorEnum wordsOperator
    ) {
        String hql =
                        " SELECT salaryCalc," +
                        " COUNT(*) as cnt" +
                        " FROM vacancy v" +
                        " WHERE 1=1";

        if (periodFrom != null) hql += " AND v.date > :periodFrom";
        if (periodTo != null) hql += " AND v.date < :periodTo";
        hql += " AND v.currency = :currency";
        if (markers.size() > 0) {
            if(SqlOperatorEnum.AND.equals(markersOperator)) hql += " AND messageid IN (SELECT vacancy_messageid FROM vacancy_markers WHERE markers IN (:markers) GROUP BY vacancy_messageid HAVING COUNT(markers)=:markersCount)";
            else hql += " AND messageid IN (SELECT vacancy_messageid FROM vacancy_markers WHERE markers IN (:markers))";
        }
        if (words.size() > 0) {
            if(SqlOperatorEnum.AND.equals(wordsOperator)) hql += " AND messageid IN (SELECT vacancy_messageid FROM vacancy_words WHERE words IN (:words) GROUP BY vacancy_messageid HAVING COUNT(words)=:wordsCount)";
            else hql += " AND messageid IN (SELECT vacancy_messageid FROM vacancy_words WHERE words IN (:words))";
        }
        if(!fromBorder) hql += " AND v.salaryTo is null";
        if(!toBorder) hql += " AND v.salaryFrom is null";
        if(!bothBorders) hql += " AND (v.salaryFrom is not null and v.salaryto is not null)";
        if(!allowPredicted) hql += " AND v.withprediction=false";
        if(salaryFrom !=null) {
            if(bothBorders) hql += " AND salaryFrom >= :salaryFrom";
            else hql += " AND salaryCalc >= :salaryFrom";
        }
        if(salaryTo !=null) {
            if(bothBorders) hql += " AND salaryTo <= :salaryTo";
            else hql += " AND salaryCalc <= :salaryTo";
        }
        hql +=  " GROUP BY salaryCalc";
        hql += " ORDER BY salaryCalc ASC";

        @NotNull final Query query = entityManager.createNativeQuery(hql);

        if (periodFrom != null) query.setParameter("periodFrom", periodFrom);
        if (periodTo != null) query.setParameter("periodTo", periodTo);
        query.setParameter("currency", currency.toString());
        if (markers.size() > 0) {
            query.setParameter("markers", markers);
            if(SqlOperatorEnum.AND.equals(markersOperator)) query.setParameter("markersCount", markers.size());

        }
        if (words.size() > 0) {
            query.setParameter("words", words);
            if(SqlOperatorEnum.AND.equals(wordsOperator)) query.setParameter("wordsCount", words.size());
        }
        if(salaryFrom !=null)  query.setParameter("salaryFrom", salaryFrom);
        if(salaryTo !=null) query.setParameter("salaryTo", salaryTo);

        @NotNull final List<Object[]> resultListFromQuery = query.getResultList();
        @NotNull final Map<Integer, Integer> resultMap = new HashMap<>();
        for (Object[] oneResultArray : resultListFromQuery) {
            resultMap.put(
                    parseInt(oneResultArray[0]),
                    parseInt(oneResultArray[1])
            );
        }
        return resultMap;
    }

    public static Integer parseInt(Object obj) {
        if(obj instanceof BigInteger) return ((BigInteger)obj).intValue();
        if(obj instanceof BigDecimal) return ((BigDecimal)obj).intValue();
        if(obj instanceof Integer) return (Integer) obj;
        if(obj instanceof Long) return ((Long) obj).intValue();
        throw new IllegalArgumentException("Cant get integer from this object!");
    }

    public @NotNull Map<Date, Integer> getStatisticByTime(
            @NotNull final List<String> markers,
            @NotNull final List<String> words,
            @Nullable final Date periodFrom,
            @Nullable final Date periodTo,
            @Nullable final Integer salaryFrom,
            @Nullable final Integer salaryTo,
            @NotNull final Boolean fromBorder,
            @NotNull final Boolean toBorder,
            @NotNull final Boolean bothBorders,
            @NotNull final Boolean allowPredicted,
            @NotNull final CurrencyEnum currency,
            @NotNull final SqlOperatorEnum markersOperator,
            @NotNull final SqlOperatorEnum wordsOperator,
            @NotNull final TimePeriodEnum timePeriod
    ) {
        String hql =
                "SELECT DATE_TRUNC(:timePeriod, date) as DateTrunc," +
                        " COUNT(*) as cnt" +
                        " FROM vacancy v" +
                        " WHERE 1=1";

        if (periodFrom != null) hql += " AND v.date > :periodFrom";
        if (periodTo != null) hql += " AND v.date < :periodTo";
        hql += " AND v.currency = :currency";
        if (markers.size() > 0) {
            if(SqlOperatorEnum.AND.equals(markersOperator)) hql += " AND messageid IN (SELECT vacancy_messageid FROM vacancy_markers WHERE markers IN (:markers) GROUP BY vacancy_messageid HAVING COUNT(markers)=:markersCount)";
            else hql += " AND messageid IN (SELECT vacancy_messageid FROM vacancy_markers WHERE markers IN (:markers))";
        }
        if (words.size() > 0) {
            if(SqlOperatorEnum.AND.equals(wordsOperator)) hql += " AND messageid IN (SELECT vacancy_messageid FROM vacancy_words WHERE words IN (:words) GROUP BY vacancy_messageid HAVING COUNT(words)=:wordsCount)";
            else hql += " AND messageid IN (SELECT vacancy_messageid FROM vacancy_words WHERE words IN (:words))";
        }
        if(!fromBorder) hql += " AND v.salaryTo is null";
        if(!toBorder) hql += " AND v.salaryFrom is null";
        if(!bothBorders) hql += " AND (v.salaryfrom is not null and v.salaryto is not null)";
        if(!allowPredicted) hql += " AND v.withprediction=false";
        if(salaryFrom !=null) {
            if(bothBorders) hql += " AND salaryFrom >= :salaryFrom";
            else hql += " AND salaryCalc >= :salaryFrom";
        }
        if(salaryTo !=null) {
            if(bothBorders) hql += " AND salaryTo <= :salaryTo";
            else hql += " AND salaryCalc <= :salaryTo";
        }
        hql +=  " GROUP BY DateTrunc";
        hql += " ORDER BY DateTrunc ASC";

        @NotNull final Query query = entityManager.createNativeQuery(hql);

        query.setParameter("timePeriod", timePeriod.toString());
        if (periodFrom != null) query.setParameter("periodFrom", periodFrom);
        if (periodTo != null) query.setParameter("periodTo", periodTo);
        query.setParameter("currency", currency.toString());
        if (markers.size() > 0) {
            query.setParameter("markers", markers);
            if(SqlOperatorEnum.AND.equals(markersOperator)) query.setParameter("markersCount", markers.size());

        }
        if (words.size() > 0) {
            query.setParameter("words", words);
            if(SqlOperatorEnum.AND.equals(wordsOperator)) query.setParameter("wordsCount", words.size());
        }
        if(salaryFrom !=null)  query.setParameter("salaryFrom", salaryFrom);
        if(salaryTo !=null) query.setParameter("salaryTo", salaryTo);

        @NotNull final List<Object[]> resultListFromQuery = query.getResultList();
        @NotNull final Map<Date, Integer> resultMap = new HashMap<>();
        for (Object[] oneResultArray : resultListFromQuery) {
            resultMap.put(
                    (Date) oneResultArray[0],
                    parseInt(oneResultArray[1])
            );
        }
        return resultMap;
    }

}
