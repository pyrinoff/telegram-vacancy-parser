package ru.pyrinoff.chatjobparser.model.endpoint;

import lombok.Getter;
import lombok.Setter;
import ru.pyrinoff.chatjobparser.enumerated.model.dto.CurrencyEnum;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class DatasetRequest {

    private List<OneLineRequest> lines;

    private CurrencyEnum salaryCurrency;

}
