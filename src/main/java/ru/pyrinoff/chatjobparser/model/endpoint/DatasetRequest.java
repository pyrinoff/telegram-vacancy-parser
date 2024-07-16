package ru.pyrinoff.chatjobparser.model.endpoint;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ru.pyrinoff.chatjobparser.enumerated.dto.CurrencyEnum;
import ru.pyrinoff.chatjobparser.enumerated.dto.TimePeriodEnum;

import java.util.List;

@Getter
@Setter
@Schema(description = "Запрос от UI на получение набора данных")
public class DatasetRequest {

    //@Schema(description = "Линии данных")
    private List<OneLineRequest> lines;

    @Schema(description = "Валюта")
    private CurrencyEnum salaryCurrency;

    @Schema(description = "Период выборки")
    private TimePeriodEnum timePeriod;

}
