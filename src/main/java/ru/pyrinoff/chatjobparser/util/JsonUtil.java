package ru.pyrinoff.chatjobparser.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface JsonUtil {

    static @Nullable String objectToJson(@Nullable Object object, boolean pretty) throws JsonProcessingException {
        if(object == null) return null;
        @NotNull final ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.registerModule(new JavaTimeModule());
        ObjectWriter objectWriter = pretty ? objectMapper.writerWithDefaultPrettyPrinter() : objectMapper.writer();
        return objectWriter.writeValueAsString(object);
    }

    static <T> T jsonToObject(@Nullable String jsonString, @NotNull Class<T> clazz) throws Exception {
        if(jsonString==null) throw new Exception("Json string is null!");
        @NotNull final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        objectMapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);
        objectMapper.enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
        return objectMapper.readValue(jsonString, clazz);
    }

}
