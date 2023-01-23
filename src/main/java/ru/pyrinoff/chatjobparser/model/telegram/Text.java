
package ru.pyrinoff.chatjobparser.model.telegram;

import javax.annotation.processing.Generated;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "type",
    "text",
    "href"
})
@Generated("jsonschema2pojo")
public class Text {

    @JsonProperty("type")
    public String type;

    @JsonProperty("text")
    public String text;

    @JsonProperty("href")
    public String href;

}
