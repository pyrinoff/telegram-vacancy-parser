package ru.pyrinoff.chatjobparser.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.pyrinoff.chatjobparser.service.ParserService;
import ru.pyrinoff.chatjobparser.service.PropertyService;
import ru.pyrinoff.chatjobparser.service.VacancyService;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class IndexController {

    @Autowired
    ParserService parserService;

    @Autowired
    VacancyService vacancyService;

    @Autowired
    PropertyService propertyService;

    private static final int LINES_COUNT = 3;

    private static Map<String, String> PERIODS = new LinkedHashMap<>() {{
        put("currentYear", "Текущий год");
        put("currentMonth", "Текущий месяц (см. дату обновления данных)");
        put("lastMonth", "Предыдущий месяц");
        put("lastThreeMonths", "Три месяца");
        put("lastSixMonths", "Полгода");
        put("lastYear", "Год");
        put("currentYearMinusOne", "Прошлый год");
        put("currentYearMinusTwo", "Позапрошлый год");
        put("all", "Весь период статистики");
        put("custom", "Кастомный период");
    }};

    private static Map<String, String> PERIODS_MONTHS = new LinkedHashMap<>() {{
        put("january", "Январь");
        put("february", "Февраль");
        put("march", "Март");
        put("april", "Апрель");
        put("may", "Май");
        put("june", "Июнь");
        put("july", "Июль");
        put("august", "Август");
        put("september", "Сентябрь");
        put("october", "Октябрь");
        put("november", "Ноябрь");
        put("december", "Декабрь");
    }};

    public static boolean UNDER_MAINTENANCE = false;

    @GetMapping("/")
    public ModelAndView index() {
        @NotNull final ModelAndView modelAndView = new ModelAndView();
        if (UNDER_MAINTENANCE) {
            modelAndView.setViewName("maintenance");
            return modelAndView;
        }
        modelAndView.setViewName("index");
        modelAndView.addObject("LINES_COUNT", LINES_COUNT);
        modelAndView.addObject("DB_ROWS_COUNT", vacancyService.getDB_ROWS_COUNT());
        modelAndView.addObject("DB_LAST_VACANCY_DATE", vacancyService.getDB_LAST_VACANCY_DATE());
        modelAndView.addObject("MARKERS", vacancyService.getMARKERS());
        modelAndView.addObject("WORDS", vacancyService.getWORDS());
        modelAndView.addObject("PERIODS", PERIODS);
        modelAndView.addObject("PERIODS_MONTHS", PERIODS_MONTHS);
        return modelAndView;
    }

}
