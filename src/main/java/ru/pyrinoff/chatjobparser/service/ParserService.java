package ru.pyrinoff.chatjobparser.service;

import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pyrinoff.chatjobparser.enumerated.model.telegram.TextTypeEnum;
import ru.pyrinoff.chatjobparser.exception.service.parser.MessageDateEmpty;
import ru.pyrinoff.chatjobparser.exception.service.parser.MessageTooSmall;
import ru.pyrinoff.chatjobparser.exception.service.parser.ParsedTextEmpty;
import ru.pyrinoff.chatjobparser.exception.service.parser.VacancyNotCorrect;
import ru.pyrinoff.chatjobparser.model.dto.Vacancy;
import ru.pyrinoff.chatjobparser.model.parser.ParserServiceResult;
import ru.pyrinoff.chatjobparser.model.telegram.*;
import ru.pyrinoff.chatjobparser.parser.marker.AbstractMarkerParser;
import ru.pyrinoff.chatjobparser.parser.salary.AbstractSalaryParser;
import ru.pyrinoff.chatjobparser.parser.salary.result.SalaryParserResult;
import ru.pyrinoff.chatjobparser.parser.word.WordParser;
import ru.pyrinoff.chatjobparser.util.FileUtils;
import ru.pyrinoff.chatjobparser.util.JsonUtil;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Service
public class ParserService {

    private static boolean SHOW_TEXT_DURING_PARSE = false;

    @Nullable final Set<String> wordsToSearch;

    private final int MIN_VACANCY_TEXT_LENGTH = 250;

    private @NotNull final List<ParserServiceResult> parserServiceResultList = new ArrayList<>();

    private @NotNull Set<ParserServiceResult> parserServiceResultSet = new HashSet<>();

    @Autowired
    private @NotNull VacancyService vacancyService;

    @Autowired
    private @NotNull List<AbstractSalaryParser> salaryParserList;

    @Autowired
    private @NotNull List<AbstractMarkerParser> markerParserList;

    private @Nullable ChatExportJson chatExportJson;

    @SneakyThrows
    public ParserService() {
        wordsToSearch = new HashSet<>(FileUtils.fileGetContent("src/main/resources/words.txt"));
    }

    public static @Nullable String cleanupText(@Nullable String dirtyString) {
        if (dirtyString == null) return null;
        dirtyString = dirtyString.replaceAll("$|\r|\n|\\+|\\(|\\)|: | :|, | ,| \\/|\\/ ", " ");//v1 (positive)
        dirtyString = dirtyString.replaceAll("(\\d)[ \\.]{1,2}000", "$1000");
        dirtyString = dirtyString.replaceAll("\s{2,}+", " ");
        dirtyString = dirtyString.trim().toLowerCase();
        return dirtyString;
    }

    @PostConstruct
    private void postLoad() {
        System.out.println("Parsers: ");
        salaryParserList.forEach(System.out::println);
        System.out.println("Parsers end");
    }

    @SneakyThrows
    public void parseMarkers(@NotNull final ParserServiceResult parserServiceResult) {
        @NotNull Set<String> markersResultSet = new HashSet<>();

        for (@NotNull final AbstractMarkerParser oneMarkerParser : markerParserList) {
            @Nullable String parsedMarker = oneMarkerParser.getMarker(parserServiceResult);
            if (parsedMarker == null) continue;
            markersResultSet.add(parsedMarker);
        }
        parserServiceResult.setMarkers(markersResultSet);
    }

    @SneakyThrows
    public void parseWords(@NotNull final ParserServiceResult parserServiceResult) {
        @NotNull Set<String> wordsResultSet = WordParser.parse(parserServiceResult, wordsToSearch);
        if (wordsResultSet.size() == 0) return;
        parserServiceResult.setWords(wordsResultSet);
    }

    @SneakyThrows
    public void parseSalary(@NotNull final ParserServiceResult parserServiceResult) {
        @Nullable SalaryParserResult salaryParserResult = null;
        @Nullable final String text = parserServiceResult.getText();
        if (text == null) throw new ParsedTextEmpty();

        if (AbstractSalaryParser.DEBUG) System.out.println("Text: " + text);

        for (@NotNull final AbstractSalaryParser oneSalaryParser : salaryParserList) {
            salaryParserResult = oneSalaryParser.parse(text);
            if (salaryParserResult != null) break;
        }

        if (salaryParserResult == null) throw new VacancyNotCorrect("Cant parse salary!");

        parserServiceResult.setSalaryFrom(salaryParserResult.getFrom());
        parserServiceResult.setSalaryTo(salaryParserResult.getTo());
        parserServiceResult.setCurrency(salaryParserResult.getCurrencyEnum());
        parserServiceResult.setWithPrediction(salaryParserResult.getWithPrediction());
    }

    @SneakyThrows
    public void parseFileToMemory(@NotNull final String chatExportJsonFilepath) {
        final @Nullable String fileContent = FileUtils.fileGetContentAsString(chatExportJsonFilepath);
        if (fileContent == null) throw new RuntimeException("No content in json file: " + chatExportJsonFilepath);
        chatExportJson = JsonUtil.jsonToObject(fileContent, ChatExportJson.class);
        System.out.println("Loaded fron file: " + chatExportJson.getMessages().size());
    }

    public void filterById(@NotNull final Integer messageId) {
        @NotNull final List<Message> newList = chatExportJson.getMessages().stream().filter(oneMessage -> messageId.equals(oneMessage.getId())).collect(Collectors.toList());
        System.out.println(newList.size());
        System.out.println("--------");
        chatExportJson.setMessages(newList);
    }

    @SneakyThrows
    public void parseVacancies(@NotNull String filepath, @Nullable Integer filterById, final boolean writeToDb) {
        System.out.println("Starting parse vacancies");
        parseFileToMemory(filepath);
        if (filterById != null) filterById(filterById);
        parseVacanciesFromTelegramHistory();
        makeVacanciesUnique();
        if (writeToDb) saveVacanciesToDb();
    }

    @SneakyThrows
    public void parseVacanciesFromTelegramHistory() {
        if (chatExportJson == null)
            throw new Exception("Chat history not loaded to memory! Use parseFileToMemory() first!");
        parserServiceResultList.clear();
        parserServiceResultSet.clear();

        for (Message oneTelegramMessage : chatExportJson.getMessages()) {
            try {
                @NotNull final ParserServiceResult parserServiceResult = parseTgMessageToResult(oneTelegramMessage);
                parserServiceResultList.add(parserServiceResult);
            } catch (@NotNull final Exception e) {
                if (e instanceof MessageTooSmall || e instanceof ParsedTextEmpty) continue;
                if (e instanceof VacancyNotCorrect) {
                    System.out.println("SKIP MESSAGE #" + oneTelegramMessage.getId());
                    continue;
                }
                System.out.println("Exception during parse message #" + oneTelegramMessage.getId());
                e.printStackTrace();
                //if (true) break;
                continue;
            }
        }
        System.out.println("Parsing ended, processed: " + chatExportJson.getMessages().size() + ", stored: " + parserServiceResultList.size());
    }

    @SneakyThrows
    public @NotNull ParserServiceResult parseTgMessageToResult(@NotNull final Message message) {
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parseDate(message, parserServiceResult);
        parseText(message, parserServiceResult, MIN_VACANCY_TEXT_LENGTH);
        parseSalary(parserServiceResult);
        parseMarkers(parserServiceResult);
        parseWords(parserServiceResult);
        return parserServiceResult;
    }

    private void parseDate(@NotNull final Message message, @NotNull final ParserServiceResult parserServiceResult) {
        @Nullable Date date = Date.from(Instant.ofEpochSecond(Long.parseLong(message.getDateUnixtime())));
        if (date == null) throw new MessageDateEmpty();
        parserServiceResult.setDate(date);
    }

    public void parseText(@NotNull final Message message, @NotNull final ParserServiceResult parserServiceResult, final int minLength) {
        @Nullable String text = cleanupText(getTextFromTextList(message.getTextEntities(), true));
        if (text == null) throw new ParsedTextEmpty();
        if (text.length() < minLength) throw new MessageTooSmall();
        parserServiceResult.setText(text);
    }

    private @Nullable String getTextFromTextList(@Nullable final List<TextEntity> textList, final boolean skipLinks) {
        if (textList == null || textList.isEmpty()) return null;
        StringBuilder sb = new StringBuilder();
        for (@NotNull final TextEntity oneText : textList) {
            if (skipLinks && TextTypeEnum.valueOf(oneText.getType()) == TextTypeEnum.LINK) continue;
            sb.append(oneText.getText());
        }
        if (sb.isEmpty()) return null;
        return sb.toString();
    }

    private void makeVacanciesUnique() {
        if (parserServiceResultList == null || parserServiceResultList.isEmpty()) return;
        parserServiceResultSet = new HashSet<>(parserServiceResultList);
        System.out.println("Duplicates deleted, before: " + parserServiceResultList.size() + ", after: " + parserServiceResultSet.size());
    }

    private @Nullable Vacancy mapResultToVacancy(@Nullable final ParserServiceResult parserServiceResult) {
        if (parserServiceResult == null) return null;
        parserServiceResultSet = new HashSet<>(parserServiceResultList);
        @NotNull final Vacancy vacancy = new Vacancy();
        vacancy.setDate(parserServiceResult.getDate());
        vacancy.setSalaryFrom(parserServiceResult.getSalaryFrom());
        vacancy.setSalaryTo(parserServiceResult.getSalaryTo());
        vacancy.setCurrency(parserServiceResult.getCurrency());
        vacancy.setMarkers(parserServiceResult.getMarkers());
        vacancy.setWords(parserServiceResult.getWords());
        return vacancy;
    }

    private void saveVacanciesToDb() {
        if (parserServiceResultSet == null || parserServiceResultSet.isEmpty()) return;
        @NotNull final List<Vacancy> vacancies = Collections.emptyList();
        for (@NotNull final ParserServiceResult oneResult : parserServiceResultSet) {
            @NotNull final Vacancy vacancy = mapResultToVacancy(oneResult);
            if (vacancy != null) vacancies.add(vacancy);
        }
        vacancyService.addAll(vacancies);
    }

}
