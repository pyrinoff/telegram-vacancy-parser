package ru.pyrinoff.chatjobparser.service;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import ru.pyrinoff.chatjobparser.common.AbstractSpringTest;
import ru.pyrinoff.chatjobparser.configuration.ApplicationConfiguration;
import ru.pyrinoff.chatjobparser.configuration.WebMvcConfiguration;
import ru.pyrinoff.chatjobparser.service.charts.SalaryCountChartService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContextConfiguration(classes = { ApplicationConfiguration.class, WebMvcConfiguration.class })
public class VacancyServiceTest extends AbstractSpringTest {

    @Autowired
    private @NotNull VacancyService VACANCY_SERVICE;

    @BeforeAll
    public static void setUp() {
    }

    @Test
    public void setupOk() {
    }

    @SneakyThrows @Test
    public void convertTest1() {
        List<Map<Integer, Integer>> resultLinesList = new ArrayList<>();
        resultLinesList.add(
                new HashMap<>() {{
                    put(50000, 1);
                    put(70000, 1);
                    put(100000, 1);
                }}
        );
        resultLinesList.add(
                new HashMap<>() {{
                    put(60000, 2);
                    put(70000, 2);
                    put(90000, 2);
                }}
        );
        Object[][] actualObject = SalaryCountChartService.convertListMapResultArray(resultLinesList);
        Object[][] expectedObject = new Object[][]{
                {"Зарплата", "Линия 1", "Линия 2"},
                {50000, 1, 0},
                {60000, 0, 2},
                {70000, 1, 2},
                {90000, 0, 2},
                {100000, 1, 0}
        };
        //System.out.println(JsonUtil.objectToJson(expectedObject, true));
        //System.out.println(JsonUtil.objectToJson(actualObject, true));
        Assertions.assertArrayEquals(expectedObject, actualObject);
    }

}
