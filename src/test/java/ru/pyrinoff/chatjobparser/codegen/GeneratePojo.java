//package ru.pyrinoff.chatjobparser.codegen;
//
//import lombok.SneakyThrows;
//import org.jetbrains.annotations.Nullable;
//import ru.pyrinoff.chatjobparser.util.FileUtils;
//
//import java.io.File;
//
//public class GeneratePojo {
//
//    @SneakyThrows
//    public static void main(String[] args) {
//        generatePojoForTelegramMessage();
//    }
//
//    @SneakyThrows
//    public static void generatePojoForTelegramMessage() {
//        @Nullable final String jsonString = FileUtils.fileGetContentAsString("./src/main/resources/messageExample.json");
//        if(jsonString == null) throw new Exception("File empty!");
//        PojoGenUtil.convertJsonToJavaClass(
//                jsonString,
//                new File("./src/main/java/ru/pyrinoff/chatjobparser/model/"),
//                "generated",
//                "TelegramHistoryMessage"
//        );
//    }
//
//}
//
