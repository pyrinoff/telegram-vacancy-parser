package ru.pyrinoff.chatjobparser.model.telegram;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * If empty - deserialize to empty list.
 */
public class ListTextDeserializer extends JsonDeserializer<Object> {

    @Override
    public List<Text> deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.readValueAsTree();
        if (node.asText().isEmpty()) {
            return Collections.emptyList();
        }
        return deserialize(jsonParser, context);
    }

}
