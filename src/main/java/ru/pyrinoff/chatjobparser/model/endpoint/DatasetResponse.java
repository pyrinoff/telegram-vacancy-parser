package ru.pyrinoff.chatjobparser.model.endpoint;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatasetResponse {

    private DatasetRequest request;

    private Object[][] salaryCountChart;

    private Object[][] dateCountChart;

}
