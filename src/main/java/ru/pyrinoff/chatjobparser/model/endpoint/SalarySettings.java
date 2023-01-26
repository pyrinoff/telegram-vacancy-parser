package ru.pyrinoff.chatjobparser.model.endpoint;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalarySettings {

    private Integer salaryFrom;

    private Integer salaryTo;

    private Boolean fromBorder;

    private Boolean toBorder;

    private Boolean bothBorders;

    private Boolean allowPredicted;

}
