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

    //@NotNull final String CHAT_EXPORT_EXAMPLE = "./chatExportTest.json";
    @NotNull final String CHAT_EXPORT_EXAMPLE = "./chatExport.json";

    //@NotNull final Integer ID = 174507;
    @NotNull final Integer ID = null;

    final boolean WRITE_TO_DB = false;

    public void start(@Nullable String[] args) {
        parse();
    }

    public void parse() {
        parserService.parseVacancies(CHAT_EXPORT_EXAMPLE, ID, WRITE_TO_DB);
    }

}
