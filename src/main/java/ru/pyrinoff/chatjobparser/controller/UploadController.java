package ru.pyrinoff.chatjobparser.controller;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.pyrinoff.chatjobparser.service.ParserService;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Controller
public class UploadController {

    @Autowired
    ParserService parserService;

    @GetMapping("/upload")
    public ModelAndView uploadPage() {
        @NotNull final ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("upload");
        return modelAndView;
    }

    @PostMapping("/upload")
    public ModelAndView uploadProcessing(@RequestParam("file") MultipartFile file) {
        @NotNull final ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("upload");

        //Основная бизнес-логика
        try {
            parserService.startProcessingThread(file);
            modelAndView.addObject("message", "Файл загружен и поставлен на обработку!");
            return modelAndView;
        }
        catch (final Exception e) {
            modelAndView.addObject("message", "Ошибка: " + e.getMessage());
            return modelAndView;
        }
    }

}
