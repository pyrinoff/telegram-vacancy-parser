package ru.pyrinoff.chatjobparser.component;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.pyrinoff.chatjobparser.exception.service.parser.ParsedTextEmpty;
import ru.pyrinoff.chatjobparser.model.parser.ParserServiceResult;
import ru.pyrinoff.chatjobparser.model.telegram.Message;
import ru.pyrinoff.chatjobparser.service.ParserService;
import ru.pyrinoff.chatjobparser.util.ArrayUtil;
import ru.pyrinoff.chatjobparser.util.TextUtil;

import java.util.*;

@Component
public class WordStat {

    @Autowired
    @NotNull ParserService parserService;

//        @NotNull final String CHAT_EXPORT_EXAMPLE = "./chatExportTest.json";
    @NotNull final String CHAT_EXPORT_EXAMPLE = "./chatExport.json";

    public void start(@Nullable String[] args) {
        parserService.parseFileToMemory(CHAT_EXPORT_EXAMPLE);

        @NotNull List<String> wordList = new ArrayList<>();

        for (Message oneTelegramMessage :  parserService.getChatExportJson().getMessages()) {
            try {
                @NotNull final ParserServiceResult parserServiceResult = parserService.parseTgMessageToResult(oneTelegramMessage);
                if (parserServiceResult != null) wordList.addAll(parserServiceResult.getTextWords());
            }
            catch (@NotNull final ParsedTextEmpty e) {
                continue;
            }
        }
        System.out.println("Word list size: " + wordList.size());
        Map<String, Long> stringLongMap = ArrayUtil.countValues(wordList);
        Map<String, Long> stringLongMapSorted = ArrayUtil.sortByValue(stringLongMap);
        stringLongMapSorted.forEach( (k, v) -> System.out.println(k+"\t"+v));
    }

}
