
package ru.pyrinoff.chatjobparser.enumerated.model.telegram;

import javax.annotation.Nullable;

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

    public static @Nullable TextTypeEnum getByValue(@Nullable final String typeString) {
        if(typeString == null || typeString.isEmpty()) return null;
        for(TextTypeEnum oneValue : values()) {
            if(oneValue.getValue().equals(typeString)) return oneValue;
        }
        return null;
    }

    @Override
    public String toString() {
        return value.toLowerCase();
    }
}
