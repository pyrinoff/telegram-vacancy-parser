package ru.pyrinoff.chatjobparser.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.pyrinoff.chatjobparser.model.endpoint.MaintenanceRequest;

@Controller
@RequestMapping("/api")
public class SettingsEndpoint {

    @PutMapping("/maintenance")
    private ResponseEntity<String> edit(@RequestBody final MaintenanceRequest maintenanceRequest) {
        if(maintenanceRequest == null || maintenanceRequest.enabled == null) return ResponseEntity.badRequest().build();
        IndexController.UNDER_MAINTENANCE = maintenanceRequest.enabled;
        return ResponseEntity.ok().build();
    }

    @GetMapping("/maintenance")
    private boolean get() {
        return IndexController.UNDER_MAINTENANCE;
    }

}
