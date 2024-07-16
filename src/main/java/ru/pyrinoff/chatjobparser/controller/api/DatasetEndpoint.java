package ru.pyrinoff.chatjobparser.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pyrinoff.chatjobparser.model.endpoint.DatasetRequest;
import ru.pyrinoff.chatjobparser.model.endpoint.DatasetResponse;
import ru.pyrinoff.chatjobparser.service.VacancyService;

@RestController
@RequestMapping("/api")
@Tag(name = "Набор данных для графика", description = "Работа с данными, которые отображаются на UI в виде графика (ов)")
public class DatasetEndpoint {

    @Getter
    @Autowired
    private @NotNull VacancyService vacancyService;

    @Operation(summary = "Получить набор данных")
    @PostMapping("/dataset")
    public DatasetResponse post(@RequestBody DatasetRequest request) {
        return vacancyService.getDataset(request);
    }

}
