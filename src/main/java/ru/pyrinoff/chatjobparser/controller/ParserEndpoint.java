package ru.pyrinoff.chatjobparser.controller;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pyrinoff.chatjobparser.model.endpoint.ParserStatusResponse;
import ru.pyrinoff.chatjobparser.service.ParserService;
import ru.pyrinoff.chatjobparser.service.VacancyService;

@RestController
@RequestMapping("/api/parse")
public class ParserEndpoint {

    @Getter
    @Autowired
    private @NotNull ParserService parserService;

    @Getter
    @Autowired
    private @NotNull VacancyService vacancyService;

    @GetMapping("/status")
    @ResponseBody
    public ParserStatusResponse getStatus() {
        return new ParserStatusResponse(parserService.isProcessingStatus(), parserService.getLastProcessingResult());
    }

    @GetMapping("/clearAll")
    @ResponseBody
    public ResponseEntity clearAll() {
        vacancyService.removeAll();
        vacancyService.recalculateStatData();
        return ResponseEntity.ok().build();
    }

}
