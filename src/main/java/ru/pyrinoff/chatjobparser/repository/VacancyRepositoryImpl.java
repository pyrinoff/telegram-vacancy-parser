package ru.pyrinoff.chatjobparser.repository;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import ru.pyrinoff.chatjobparser.enumerated.dto.CurrencyEnum;
import ru.pyrinoff.chatjobparser.enumerated.dto.SqlOperatorEnum;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class VacancyRepositoryImpl implements VacancyRepositoryCustom  {

    @PersistenceContext
    EntityManager entityManager;

    public Map<Integer, Integer> getStatistic(
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
                "SELECT tSubTable.salary, tSubTable.cnt FROM (" +
                        " SELECT ROUND(" +
                            " CASE" +
                                " WHEN salaryfrom=salaryto THEN salaryfrom" +
                                " WHEN salaryfrom IS NOT NULL AND salaryto IS NOT NULL THEN (salaryto+salaryfrom)/2" +
                                " WHEN salaryfrom IS NOT NULL THEN salaryfrom" +
                                " WHEN salaryto IS NOT NULL THEN salaryto" +
                                " ELSE null" +
                            " END" +
                        ", " + getCurrencyRounder(currency) + ") as salary," +
                        " COUNT(distinct messageid) as cnt" +
                        " FROM vacancy v" +
                        //" LEFT JOIN vacancy_markers vm ON v.messageid = vm.vacancy_messageid" +
                        //" LEFT JOIN vacancy_words vw ON v.messageid = vw.vacancy_messageid" +
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

        hql +=  " GROUP BY salary";
        hql += " ) tSubTable WHERE 1=1";
        if(salaryFrom !=null) hql += " AND salary >= :salaryFrom";
        if(salaryTo !=null) hql += " AND salary <= :salaryTo";
        hql += " ORDER BY salary ASC";

        @NotNull final Query query = entityManager.createNativeQuery(hql);

        query.setParameter("currency", currency.toString());
        if (periodFrom != null) query.setParameter("periodFrom", periodFrom);
        if (periodTo != null) query.setParameter("periodTo", periodTo);
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

    public int getCurrencyRounder(CurrencyEnum currencyEnum) {
        if(CurrencyEnum.RUB.equals(currencyEnum)) return -4; //round to 10000
        else if(CurrencyEnum.USD.equals(currencyEnum)) return -2; //round to 100
        return -1; //should not be here
    }

    public static Integer parseInt(Object obj) {
        if(obj instanceof BigInteger) return ((BigInteger)obj).intValue();
        if(obj instanceof BigDecimal) return ((BigDecimal)obj).intValue();
        if(obj instanceof Integer) return (Integer) obj;
        throw new IllegalArgumentException("Cant get integer from this object!");
    }


}
