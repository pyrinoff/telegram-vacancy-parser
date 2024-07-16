package ru.pyrinoff.chatjobparser.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pyrinoff.chatjobparser.controller.view.IndexController;
import ru.pyrinoff.chatjobparser.model.endpoint.MaintenanceRequest;

@RestController
@Tag(name = "Настройки")
@RequestMapping("/admin")
public class SettingsEndpoint {

    @Operation(summary = "Установка режима технических работ")
    @PutMapping("/maintenance")
    private ResponseEntity<String> edit(
            @Schema(name = "Запрос режима технических работ")
            @RequestBody final MaintenanceRequest maintenanceRequest
    ) {
        if(maintenanceRequest == null || maintenanceRequest.enabled == null) return ResponseEntity.badRequest().build();
        IndexController.UNDER_MAINTENANCE = maintenanceRequest.enabled;
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Проверка статуса режима технических работ")
    @GetMapping("/maintenance")
    private boolean get() {
        return IndexController.UNDER_MAINTENANCE;
    }

}
