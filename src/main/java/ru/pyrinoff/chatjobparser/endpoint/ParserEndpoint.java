package ru.pyrinoff.chatjobparser.endpoint;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pyrinoff.chatjobparser.model.endpoint.DatasetRequest;
import ru.pyrinoff.chatjobparser.model.endpoint.DatasetResponse;
import ru.pyrinoff.chatjobparser.service.ParserService;
import ru.pyrinoff.chatjobparser.service.VacancyService;

@RestController
@RequestMapping("/api")
public class ParserEndpoint {

    @Getter
    @Autowired
    private @NotNull ParserService parserService;

    @GetMapping("/parseTest")
    @ResponseBody
    public ResponseEntity get() {
        parserService.parseVacancies("d:/dev/java/Projects/JobParser/java_sources/jobstat/chatExportTest.json", null, true);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/parseTest2")
    @ResponseBody
    public ResponseEntity get2() {
        parserService.parseVacancies("d:/dev/java/Projects/JobParser/java_sources/jobstat/chatExport.json", null, true);
        return ResponseEntity.ok().build();
    }

}
