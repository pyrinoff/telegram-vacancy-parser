package ru.pyrinoff.chatjobparser.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.pyrinoff.chatjobparser.model.endpoint.ParserStatusResponse;
import ru.pyrinoff.chatjobparser.model.endpoint.UploadResponse;
import ru.pyrinoff.chatjobparser.service.ParserService;
import ru.pyrinoff.chatjobparser.service.VacancyService;

@RestController
@Tag(name = "Загрузка набора данных для графика", description = "")
@RequestMapping("/admin/parser")
public class ParserEndpoint {

    @Getter
    @Autowired
    private @NotNull ParserService parserService;

    @Getter
    @Autowired
    private @NotNull VacancyService vacancyService;

    @Operation(summary = "Загрузка набора данных")
    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadResponse> uploadProcessing(
            @Parameter(description = "Файл JSON-выгрузки из Telegram")
            @RequestParam("file") MultipartFile file
    ) {
        try {
            parserService.startProcessingThread(file);
            return ResponseEntity.ok(new UploadResponse("Файл загружен и поставлен на обработку!"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UploadResponse("Ошибка: " + e.getMessage()));
        }
    }

    @GetMapping("/status")
    @Operation(summary = "Проверка статуса")
    @ResponseBody
    public ParserStatusResponse getStatus() {
        return new ParserStatusResponse(parserService.isProcessingStatus(), parserService.getLastProcessingResult());
    }

    @GetMapping("/clear")
    @Operation(summary = "Удаление набора данных из БД")
    @ResponseBody
    public ResponseEntity clear() {
        vacancyService.removeAll();
        vacancyService.recalculateStatData();
        return ResponseEntity.ok().build();
    }

}
