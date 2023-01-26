package ru.pyrinoff.chatjobparser.model.endpoint;

import lombok.Getter;

import java.util.List;

@Getter
public class TestRequest {

    private List<String> lines;

    @Override public String toString() {
        return "TestRequest{" +
                "lines=" + lines +
                '}';
    }

}
