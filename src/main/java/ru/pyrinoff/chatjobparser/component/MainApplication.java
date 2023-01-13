package ru.pyrinoff.chatjobparser.component;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.pyrinoff.chatjobparser.service.ParserService;

@Component
public class MainApplication {

    @Autowired
    @NotNull ParserService parserService;

//    @NotNull final String CHAT_EXPORT_EXAMPLE = "./chatExportTest.json";
    @NotNull final String CHAT_EXPORT_EXAMPLE = "./chatExport.json";

//    @NotNull final Integer ID = 178724;
    @NotNull final Integer ID = null;

    public void start(@Nullable String[] args) {
        parserService.parseFileToMemory(CHAT_EXPORT_EXAMPLE);
        if(ID != null) parserService.filterById(ID);
        parserService.parseVacancies();

    }

}
