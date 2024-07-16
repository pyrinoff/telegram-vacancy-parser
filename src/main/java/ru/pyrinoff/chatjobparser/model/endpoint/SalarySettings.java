package ru.pyrinoff.chatjobparser.model.endpoint;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Данные по ЗП")
public class SalarySettings {

    @Schema(description = "Зарплата, от")
    private Integer salaryFrom;

    @Schema(description = "Зарплата, до")
    private Integer salaryTo;

    @Schema(description = "Учитывать границу 'от'")
    private Boolean fromBorder;

    @Schema(description = "Учитывать границу 'до'")
    private Boolean toBorder;

    @Schema(description = "Учитывать обе границы")
    private Boolean bothBorders;

    @Schema(description = "Разрешить неточные данные")
    private Boolean allowPredicted;

}
