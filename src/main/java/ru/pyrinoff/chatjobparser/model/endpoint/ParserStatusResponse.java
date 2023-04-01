package ru.pyrinoff.chatjobparser.model.endpoint;

import lombok.Getter;

@Getter
public class ParserStatusResponse {

    private Boolean processing;

    private String result;

    public ParserStatusResponse(Boolean processing, String result) {
        this.processing = processing;
        this.result = result;
    }

}
