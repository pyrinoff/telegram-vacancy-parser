package ru.pyrinoff.chatjobparser.model.endpoint;

import lombok.Getter;

@Getter
public class ParserStatusResponse {

    private Boolean processing;

    private Boolean result;

    public ParserStatusResponse(Boolean processing, Boolean result) {
        this.processing = processing;
        this.result = result;
    }

}
