package ru.pyrinoff.chatjobparser.controller;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.pyrinoff.chatjobparser.model.endpoint.DatasetRequest;
import ru.pyrinoff.chatjobparser.model.endpoint.DatasetResponse;
import ru.pyrinoff.chatjobparser.service.VacancyService;

@RestController
@RequestMapping("/api")
public class DatasetEndpoint {

    @Getter
    @Autowired
    private @NotNull VacancyService vacancyService;

    @PostMapping("/dataset")
    public DatasetResponse post(@RequestBody DatasetRequest request) {
        return vacancyService.getDataset(request);
    }

    @GetMapping("/dataset")
    @ResponseBody
    public String get() {
      return "WORK";
    }

}
