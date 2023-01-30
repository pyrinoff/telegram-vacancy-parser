package ru.pyrinoff.chatjobparser.controller;

import org.springframework.web.bind.annotation.*;
import ru.pyrinoff.chatjobparser.util.MemoryUsageUtil;

@RestController
@RequestMapping("/api")
public class TestEndpoint {

   @GetMapping("/info")
    private String info() {
        return MemoryUsageUtil.getMemoryUsageMb()+" MB";
   }

}
