package ru.pyrinoff.chatjobparser.service;

import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ru.pyrinoff.chatjobparser.model.telegram.ChatExportJson;
import ru.pyrinoff.chatjobparser.util.FileUtils;
import ru.pyrinoff.chatjobparser.util.JsonUtil;

@Getter
@Service
public class ParserService {

    @SneakyThrows
    public void parseFile(String chatExportJsonFilepath) {
        final @Nullable String fileContent = FileUtils.fileGetContentAsString(chatExportJsonFilepath);
        if (fileContent == null) throw new RuntimeException("No content in json file: " + chatExportJsonFilepath);
        @NotNull final ChatExportJson chatExportJson =
                JsonUtil.jsonToObject(fileContent, ChatExportJson.class);
        System.out.println(chatExportJson.getMessages().get(0));
    }

}
