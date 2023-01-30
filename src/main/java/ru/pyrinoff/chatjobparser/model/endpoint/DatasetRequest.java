package ru.pyrinoff.chatjobparser.model.endpoint;

import lombok.Getter;
import lombok.Setter;
import ru.pyrinoff.chatjobparser.enumerated.dto.CurrencyEnum;

import java.util.List;

@Getter
@Setter
public class DatasetRequest {

    private List<OneLineRequest> lines;

    private CurrencyEnum salaryCurrency;

}
