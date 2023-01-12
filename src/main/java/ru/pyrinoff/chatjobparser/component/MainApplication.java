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

    @NotNull final String CHAT_EXPORT_EXAMPLE = "./chatExportTest.json";

    public void start(@Nullable String[] args) {
        parserService.parseFile(CHAT_EXPORT_EXAMPLE);
    }

}
