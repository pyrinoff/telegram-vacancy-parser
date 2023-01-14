package ru.pyrinoff.chatjobparser.service;

import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pyrinoff.chatjobparser.exception.service.parser.MessageDateEmpty;
import ru.pyrinoff.chatjobparser.exception.service.parser.MessageTooSmall;
import ru.pyrinoff.chatjobparser.exception.service.parser.ParsedTextEmpty;
import ru.pyrinoff.chatjobparser.exception.service.parser.VacancyNotCorrect;
import ru.pyrinoff.chatjobparser.model.dto.Vacancy;
import ru.pyrinoff.chatjobparser.model.telegram.*;
import ru.pyrinoff.chatjobparser.parser.marker.AbstractMarkerParser;
import ru.pyrinoff.chatjobparser.parser.salary.AbstractSalaryParser;
import ru.pyrinoff.chatjobparser.parser.salary.result.SalaryParserResult;
import ru.pyrinoff.chatjobparser.util.FileUtils;
import ru.pyrinoff.chatjobparser.util.JsonUtil;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Service
public class ParserService {

    @Autowired
    private @NotNull VacancyService vacancyService;

    @Autowired
    private @NotNull List<AbstractSalaryParser> salaryParserList;

    @Autowired
    private @NotNull List<AbstractMarkerParser> markerParserList;

    private @Nullable ChatExportJson chatExportJson;

    private final int MIN_VACANCY_TEXT_LENGTH = 250;

    @PostConstruct
    private void postLoad() {
        System.out.println("Parsers: ");
        salaryParserList.forEach(System.out::println);
        System.out.println("Parsers end");
    }

    @SneakyThrows
    public void parseMarkers(@NotNull final String text, @NotNull final Vacancy vacancy) {
        @NotNull Set<String> markersResultSet = new HashSet<>();
        for(@NotNull final AbstractMarkerParser oneMarkerParser : markerParserList) {
            @Nullable String parsedMarker = oneMarkerParser.getMarker(text);
            if(oneMarkerParser == null) continue;
            markersResultSet.add(parsedMarker);
        }
        vacancy.setMarkers(markersResultSet);
    }

    @SneakyThrows
    public void parseSalary(@NotNull final String text, @NotNull final Vacancy vacancy) {
        @Nullable SalaryParserResult salaryParserResult = null;
        if(AbstractSalaryParser.DEBUG) System.out.println("Text: "+text);

        for(@NotNull final AbstractSalaryParser oneSalaryParser : salaryParserList) {
            salaryParserResult = oneSalaryParser.parse(text);
            if(salaryParserResult != null) break;
        }

        if(salaryParserResult == null) throw new VacancyNotCorrect("Cant parse salary!");

        vacancy.setSalaryFrom(salaryParserResult.getFrom());
        vacancy.setSalaryTo(salaryParserResult.getTo());
        vacancy.setCurrency(salaryParserResult.getCurrencyEnum());
        vacancy.setWithPrediction(salaryParserResult.getWithPrediction());
    }

    @SneakyThrows
    public void parseFileToMemory(String chatExportJsonFilepath) {
        final @Nullable String fileContent = FileUtils.fileGetContentAsString(chatExportJsonFilepath);
        if (fileContent == null) throw new RuntimeException("No content in json file: " + chatExportJsonFilepath);
        chatExportJson = JsonUtil.jsonToObject(fileContent, ChatExportJson.class);
    }

    public void filterById(@NotNull final Integer messageId) {
        @NotNull final List<Message> newList = chatExportJson.getMessages().stream().filter(oneMessage -> messageId.equals(oneMessage.getId())).collect(Collectors.toList());
        System.out.println(newList.size());
        System.out.println("--------");
        chatExportJson.setMessages(newList);
    }

    @SneakyThrows
    public void parseVacancies() {
        System.out.println("Starting parse vacancies");
        if (chatExportJson == null)
            throw new Exception("Chat history not loaded to memory! Use parseFileToMemory() first!");
        int stored = 0;
        int index = 0;
        for (Message oneTelegramMessage : chatExportJson.getMessages()) {
            index++;
            try {
                @Nullable Vacancy vacancy = parseMessageToVacancy(oneTelegramMessage);
                System.out.println("PARSED SUCCESSFULLY, id: "+ oneTelegramMessage.getId() +", result: "+vacancy.getSalaryFrom()+"-"+vacancy.getSalaryTo()+" "+vacancy.getCurrency());
                //vacancyService.add(vacancy);
                stored++;
            } catch (@NotNull final Exception e) {
                if (e instanceof MessageTooSmall) continue;
                if (e instanceof VacancyNotCorrect) {
                    System.out.println("SKIP MESSAGE #" + oneTelegramMessage.getId());
                    continue;
                }
                System.out.println("Exception during parse message #" + index);
                e.printStackTrace();
                //if (true) break;
                continue;
            }
        }
        System.out.println("Parsing ended, processed: " + index + ", stored: " + stored);
    }

    @SneakyThrows
    private @NotNull Vacancy parseMessageToVacancy(Message message) {
        @NotNull final Vacancy vacancy = new Vacancy();
        //DATE
        parseDate(message, vacancy);
        //TEXT
        @Nullable String text = cleanupText(parseText(message));
        if(text == null || text.length() < MIN_VACANCY_TEXT_LENGTH) throw new MessageTooSmall();
        System.out.println("TEXT: " + text);
        //SALARY
        parseSalary(text, vacancy);
        if (vacancy.getSalaryFrom() == null && vacancy.getSalaryTo() == null || vacancy.getCurrency() == null || vacancy.getWithPrediction() == null)
            throw new VacancyNotCorrect("Cant parse vacancy salary!");
        System.out.println("Salary parsed, from: " + vacancy.getSalaryFrom() + ", to: " + vacancy.getSalaryTo() + ", currency: " + vacancy.getCurrency());
        //MARKERS
        parseMarkers(text, vacancy);

        return vacancy;
    }

    private @Nullable String getTextFromTextList(List<TextEntity> textList) {
        if (textList == null || textList.isEmpty()) return null;
        StringBuilder sb = new StringBuilder();
        for (@NotNull final TextEntity oneText : textList) {
            //if(TextTypeEnum.valueOf(oneText.getType()) == TextTypeEnum.MENTION) continue;
            sb.append(oneText.getText());
        }
        if (sb.isEmpty()) return null;
        return sb.toString();
    }

    public static @Nullable String cleanupText(@NotNull String dirtyString) {
        dirtyString = dirtyString.replaceAll("$|\r|\n|\\+|\\(|\\)|: | :|, | ,| \\/|\\/ ", " ");//v1 (positive)
        dirtyString = dirtyString.replaceAll("(\\d)[ \\.]{1,2}000", "$1000");
        dirtyString = dirtyString.replaceAll("\s{2,}+", " ");
        dirtyString = dirtyString.trim().toLowerCase();
        return dirtyString;
    }

    private @Nullable void parseDate(@NotNull final Message message, @NotNull final Vacancy vacancy) {
        @Nullable Date date = Date.from(Instant.ofEpochSecond(Long.parseLong(message.getDateUnixtime())));
        if (date == null) throw new MessageDateEmpty();
        vacancy.setDate(date);
    }

    private @Nullable String parseText(@NotNull Message message) {
        @Nullable String text = getTextFromTextList(message.getTextEntities());
        if (text == null) throw new ParsedTextEmpty();
        return text;
    }


}
