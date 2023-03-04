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
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
