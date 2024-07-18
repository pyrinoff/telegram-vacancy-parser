package ru.pyrinoff.chatjobparser.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.pyrinoff.chatjobparser.component.parser.marker.AbstractMarkerParser;
import ru.pyrinoff.chatjobparser.component.parser.salary.AbstractSalaryParser;
import ru.pyrinoff.chatjobparser.component.parser.salary.result.SalaryParserResult;
import ru.pyrinoff.chatjobparser.component.parser.word.WordParser;
import ru.pyrinoff.chatjobparser.enumerated.dto.CurrencyEnum;
import ru.pyrinoff.chatjobparser.enumerated.telegram.TextTypeEnum;
import ru.pyrinoff.chatjobparser.exception.service.parser.MessageDateEmpty;
import ru.pyrinoff.chatjobparser.exception.service.parser.MessageTooSmall;
import ru.pyrinoff.chatjobparser.exception.service.parser.ParsedTextEmpty;
import ru.pyrinoff.chatjobparser.exception.service.parser.VacancyNotCorrect;
import ru.pyrinoff.chatjobparser.model.dto.Vacancy;
import ru.pyrinoff.chatjobparser.model.parser.ParserServiceResult;
import ru.pyrinoff.chatjobparser.model.telegram.ChatExportJson;
import ru.pyrinoff.chatjobparser.model.telegram.Message;
import ru.pyrinoff.chatjobparser.model.telegram.TextEntity;
import ru.pyrinoff.chatjobparser.util.FileUtils;
import ru.pyrinoff.chatjobparser.util.JsonUtil;
import ru.pyrinoff.chatjobparser.util.NumberUtil;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Service
@Slf4j
public class ParserService {

    @Nullable
    final Set<String> wordsToSearch;

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

    private boolean processingStatus = false;

    private String lastProcessingResult = "файл еще не загружался";

    private Thread processingThread = null;

    @SneakyThrows
    public ParserService() {
        wordsToSearch = new HashSet<>(FileUtils.fileGetContentFromJar("words.txt"));
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
        if (AbstractSalaryParser.DEBUG) {
            System.out.println("Parsers: ");
            salaryParserList.forEach(System.out::println);
            System.out.println("Parsers end");
        }
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

    public static int getCurrencyRounder(CurrencyEnum currencyEnum) {
        if(CurrencyEnum.RUB.equals(currencyEnum)) return -4; //round to 10000
        else if(CurrencyEnum.USD.equals(currencyEnum)) return -2; //round to 100
        return -1; //should not be here
    }

    private static Integer getCalcSalary(@Nullable Integer from, @Nullable Integer to, @Nullable CurrencyEnum currencyEnum) {
        Integer result = null;
        if (from != null && to != null) result =  (from + to) / 2;
        if (from != null) result =  from;
        if (to != null) result =  to;
        if(result == null) return null;
        return (int) NumberUtil.round(result, getCurrencyRounder(currencyEnum));
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

        Integer salaryCalc = getCalcSalary(
                salaryParserResult.getFrom()
                , salaryParserResult.getTo()
                , salaryParserResult.getCurrencyEnum()
        );
        parserServiceResult.setSalaryFrom(salaryParserResult.getFrom());
        parserServiceResult.setSalaryTo(salaryParserResult.getTo());
        parserServiceResult.setSalaryCalc(salaryCalc);
        parserServiceResult.setCurrency(salaryParserResult.getCurrencyEnum());
        parserServiceResult.setWithPrediction(salaryParserResult.getWithPrediction());
    }

    @SneakyThrows
    public void parseFileToMemory(@NotNull final String chatExportJsonFilepath) {
        final @Nullable String fileContent = FileUtils.fileGetContentAsString(chatExportJsonFilepath);
        if (fileContent == null) throw new RuntimeException("No content in json file: " + chatExportJsonFilepath);
        chatExportJson = JsonUtil.jsonToObject(fileContent, ChatExportJson.class);
        System.out.println("Loaded from file: " + chatExportJson.getMessages().size());
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
        processingStatus = true;
        lastProcessingResult = null;
        try {
            parseFileToMemory(filepath);
            if (filterById != null) filterById(filterById);
            parseVacanciesFromTelegramHistory();
            makeVacanciesUnique();
            if (writeToDb) saveVacanciesToDb();
        } catch (@NotNull final Exception e) {
            lastProcessingResult = "ERROR: " + e.getMessage();
            throw e;
        } finally {
            parserServiceResultList.clear();
            parserServiceResultSet.clear();
            chatExportJson = null;
            System.gc();
            processingStatus = false;
        }
        lastProcessingResult = "успешно";
        vacancyService.recalculateStatData();
        System.out.println("Vacancy parser stopped.");
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
                    if (AbstractSalaryParser.DEBUG) System.out.println("SKIP MESSAGE #" + oneTelegramMessage.getId());
                    continue;
                }
                System.out.println("Exception during parse message #" + oneTelegramMessage.getId());
                e.printStackTrace();
                //if (true) break;
                continue;
            }
        }
        System.out.println("Parsing ended, processed: " + chatExportJson.getMessages().size() + ", stored: " + parserServiceResultList.size());
        chatExportJson = null; //clean memory
    }

    @SneakyThrows
    public @NotNull ParserServiceResult parseTgMessageToResult(@NotNull final Message message) {
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parseDate(message, parserServiceResult);
        parseText(message, parserServiceResult, MIN_VACANCY_TEXT_LENGTH);
        parseSalary(parserServiceResult);
        parseMarkers(parserServiceResult);
        parseWords(parserServiceResult);
        parserServiceResult.setMessageId(message.getId());
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

    private @Nullable String getTextFromTextList(@Nullable final List<TextEntity> textList, final boolean skipNonImportantTypes
    ) {
        if (textList == null || textList.isEmpty()) return null;
        StringBuilder sb = new StringBuilder();
        for (@NotNull final TextEntity oneText : textList) {
            if (skipNonImportantTypes &&
                    (TextTypeEnum.getByValue(oneText.getType()) == TextTypeEnum.LINK
                            || TextTypeEnum.getByValue(oneText.getType()) == TextTypeEnum.PHONE
                            || TextTypeEnum.getByValue(oneText.getType()) == TextTypeEnum.EMAIL)
            ) continue;
            sb.append(oneText.getText());
        }
        if (sb.isEmpty()) return null;
        return sb.toString();
    }

    private void makeVacanciesUnique() {
        System.out.println("Starting de-duplication.");
        if (parserServiceResultList == null || parserServiceResultList.isEmpty()) return;
        parserServiceResultSet = new HashSet<>(parserServiceResultList);
        System.out.println("Duplicates ok, before: " + parserServiceResultList.size() + ", after: " + parserServiceResultSet.size());
        parserServiceResultList.clear(); //clear memory
    }

    private @Nullable Vacancy mapResultToVacancy(@Nullable final ParserServiceResult parserServiceResult) {
        if (parserServiceResult == null) return null;
        @NotNull final Vacancy vacancy = new Vacancy();
        vacancy.setDate(parserServiceResult.getDate());
        vacancy.setSalaryFrom(parserServiceResult.getSalaryFrom());
        vacancy.setSalaryTo(parserServiceResult.getSalaryTo());
        vacancy.setSalaryCalc(parserServiceResult.getSalaryCalc());
        vacancy.setCurrency(parserServiceResult.getCurrency());
        vacancy.setMarkers(parserServiceResult.getMarkers());
        vacancy.setWords(parserServiceResult.getWords());
        vacancy.setWithPrediction(parserServiceResult.getWithPrediction());
        vacancy.setMessageId(parserServiceResult.getMessageId());
        return vacancy;
    }

    private void saveVacanciesToDb() {
        System.out.println("Starting save to DB.");
        if (parserServiceResultSet == null || parserServiceResultSet.isEmpty()) return;

        //Map to DTO
        @NotNull final List<Vacancy> vacancies = new ArrayList<>();
        for (@NotNull final ParserServiceResult oneResult : parserServiceResultSet) {
            @NotNull final Vacancy vacancy = mapResultToVacancy(oneResult);
            if (vacancy != null) {
                vacancies.add(vacancy);
                //vacancyService.add(vacancy); //как оказалось, памяти жрет столько же, а выполняется чуть дольше
            }
        }
        parserServiceResultSet.clear(); //optimization

        //Send to DB
        System.out.println("Mapped parsed results to vacancy: " + vacancies.size());
        vacancyService.addAll(vacancies);
        System.out.println("Saved to DB.");
    }

    public void startProcessingThread(@NotNull final MultipartFile uploadedFile) throws Exception {
        if(processingThread != null && processingThread.isAlive()) throw new Exception("Обработка уже идет, дождитесь её окончания!");

        //Подготовка файла к обработке
        if (uploadedFile.isEmpty()) throw  new Exception("Файл пуст!");
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        String fileName = UUID.randomUUID() + "_" + uploadedFile.getOriginalFilename();
        File temporaryFile = new File(tempDir, fileName);
        try {
            uploadedFile.transferTo(temporaryFile);
        } catch (final IOException e) {
            throw new Exception("Ошибка при загрузке файла: " + e);
        }

        //Тред обработки
        processingThread = new Thread(() -> {
            try {
                parseVacancies(temporaryFile.getAbsolutePath(), null, true);
            }
            finally {
                temporaryFile.delete();
            }
        });
        processingThread.start();
    }

}
