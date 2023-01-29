package ru.pyrinoff.chatjobparser.controller;

import lombok.extern.log4j.Log4j2;
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
        //modelAndView.addObject("WORDS", WORDS);
        return modelAndView;
    }

    @PostMapping("/upload")
    public ModelAndView uploadProcessing(@RequestParam("file") MultipartFile file) {
        @NotNull final ModelAndView modelAndView = new ModelAndView();
        if (file.isEmpty()) {
            modelAndView.setViewName("upload");
            modelAndView.addObject("message", "Файл пуст!");
            return modelAndView;
        }

        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        File uploadedFile = new File(tempDir, fileName);
        try {
            file.transferTo(uploadedFile);
        } catch (IOException e) {
            modelAndView.addObject("message", "Ошибка при загрузке файла: " + e);
            return modelAndView;
        }
        new Thread(() -> {
            try {
                parserService.parseVacancies(uploadedFile.getAbsolutePath(), null, true);
            }
            catch (@NotNull final Exception e) {
                log.info(e.toString());
            }
            finally {
                uploadedFile.delete();
            }

        }).start();
        modelAndView.addObject("message", "Файл загружен и поставлен на обработку: "+uploadedFile);
        return modelAndView;
    }

}
