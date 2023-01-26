package ru.pyrinoff.chatjobparser.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ru.pyrinoff.chatjobparser.model.endpoint.DatasetRequest;
import ru.pyrinoff.chatjobparser.service.ParserService;
import ru.pyrinoff.chatjobparser.service.VacancyService;

import javax.annotation.PostConstruct;
import java.util.Date;

@Controller
public class IndexController {

    @Autowired
    ParserService parserService;

    @Autowired
    VacancyService vacancyService;

    private static final int LINES_COUNT = 3;

    private static long DB_ROWS_COUNT;

    private static Date DB_LAST_VACANCY_DATE;

    @PostConstruct
    public void postConstruct() {
        DB_ROWS_COUNT = vacancyService.getRowsCount();
        DB_LAST_VACANCY_DATE = vacancyService.getLastVacancyDate();

    }

    @GetMapping("/")
    public ModelAndView index() {
        @NotNull final ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("LINES_COUNT", LINES_COUNT);
        modelAndView.addObject("DB_ROWS_COUNT", DB_ROWS_COUNT);
        modelAndView.addObject("DB_LAST_VACANCY_DATE", DB_LAST_VACANCY_DATE);
        return modelAndView;
    }

}
