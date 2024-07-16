package ru.pyrinoff.chatjobparser.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pyrinoff.chatjobparser.util.MemoryUsageUtil;

@RestController
@Tag(name = "Дополнительные функции")
@RequestMapping("/admin")
public class UtilEndpoint {

    @Operation(summary = "Проверка потребляемой памяти")
    @GetMapping("/info")
    private String info() {
        return MemoryUsageUtil.getMemoryUsageMb() + " MB";
    }

}
