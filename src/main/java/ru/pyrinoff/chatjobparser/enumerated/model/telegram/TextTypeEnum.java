
package ru.pyrinoff.chatjobparser.enumerated.model.telegram;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import javax.annotation.processing.Generated;

public enum TextTypeEnum {

    HASHTAG("hashtag")
    , BOLD("bold")
    , LINK("link")
    , MENTION("mention")
    , PLAIN("plain")
    , ITALIC("italic")
    , UNDERLINE("underline");

    private final String value;

    TextTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toLowerCase();
    }
}
